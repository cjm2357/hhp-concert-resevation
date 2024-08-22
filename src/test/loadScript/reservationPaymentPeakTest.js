import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';
import {randomIntBetween, randomItem} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

let tokenCreateTime = new Trend('token_create_time');
let tokenCreateSuccessRate = new Rate('token_create_success_rate')
let tokenStatusTime = new Trend('token_status_time');
let tokenStatusSuccessRate = new Rate('token_status_success_rate')
let scheduleTime = new Trend('schedule_time');
let scheduleSuccessRate = new Rate('schedule_success_rate')
let seatTime = new Trend('seat_time');
let seatSuccessRate = new Rate('seat_success_rate')
let reservationTime = new Trend('reservation_time');
let reservationSuccessRate = new Rate('reservation_success_rate')
let paymentTime = new Trend('payment_time');
let paymentSuccessRate = new Rate('payment_success_rate')



export const options = {
    stages: [
        { duration: '1m', target: 100 }, // ramp-up to 100 users
        { duration: '3m', target: 300 }, // stay at 100 users
        { duration: '1m', target: 100 }, // ramp-up to 100 users
    ],
};
export default function () {
    let userId = randomIntBetween(1, 50);
    const tokenPayload = JSON.stringify({
        userId : userId,
    })

    const tokenParams = {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : ''
        }
    }

    //토큰 조회/발급
    let tokenCreateRes = http.post("http://localhost:8080/api/token", tokenPayload, tokenParams)
    let tokenCreateRate = check(tokenCreateRes, {
        'token_create_success': (r) => r.status === 200,
    })
    tokenCreateSuccessRate.add(tokenCreateRate)
    tokenCreateTime.add(tokenCreateRes.timings.duration);
    sleep(11)

    let tokenKey  = JSON.parse(tokenCreateRes.body)['key']
    tokenParams.headers['Authorization'] = tokenKey;
    let tokenStatusRes = http.get("http://localhost:8080/api/token/status", tokenParams)
    let tokenStatusRate = check(tokenCreateRes, {
        'token_status_success': (r) => r.status === 200,
    })
    tokenStatusSuccessRate.add(tokenStatusRate)
    tokenStatusTime.add(tokenStatusRes.timings.duration);
    let order = JSON.parse(tokenStatusRes.body)['order']
    if (order != 0) return;
    sleep(1)


    const scheduleParams = {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : tokenKey,
            tags: {name: 'search-schedule'}

        }
    }
    //스케줄 조회
    let scheduleRes = http.get("http://localhost:8080/api/concerts/1/schedules", scheduleParams)
    let scheduleRate = check(scheduleRes, {
        'schedule_success': (r) => r.status === 200,
    })

    scheduleSuccessRate.add(scheduleRate)
    scheduleTime.add(scheduleRes.timings.duration);
    //스케줄 선택시간
    sleep(1);

    let schedules = scheduleRes.json()['schedulesList']
    let scheduleId = randomItem(schedules)['scheduleId'];


    const seatParams = {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : tokenKey,
            tags: {name: 'search-seat'}

        }
    }
    let seatRes = http.get("http://localhost:8080/api/schedules/"+ scheduleId + "/seats", seatParams)
    let seatRate = check(scheduleRes, {
        'seat_success': (r) => r.status === 200,
    })

    seatSuccessRate.add(seatRate)
    seatTime.add(seatRes.timings.duration);
    //좌석 선택시간
    sleep(1);
    let seats = seatRes.json()['seats']
    let seatId = randomItem(seats)['seatId'];

// 예약
    const reservationPayload = JSON.stringify({
        userId : userId,
    })
    const reservationParams = {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : tokenKey,
            tags: {name: 'reservation'}

        }
    }

    let reservationRes = http.post("http://localhost:8080/api/seats/" + seatId + "/reservation", reservationPayload, reservationParams)
    let reservationRate = check(reservationRes, {
        'reservation_success': (r) => r.status === 200,
    })
    reservationSuccessRate.add(reservationRate)
    reservationTime.add(reservationRes.timings.duration)
    let reservationId = reservationRes.json()['id'];



    //결제
    //결제정보 입력시간
    sleep(3);
    const paymentPayload = JSON.stringify({
        userId : userId,
        reservationId: reservationId
    })
    const paymentParams = {
        headers : {
            'Content-Type' : 'application/json',
            'Authorization' : tokenKey,
            tags: {name: 'payment'}

        }
    }

    let paymentRes = http.post("http://localhost:8080/api/payment", paymentPayload, paymentParams)
    let paymentRate = check(paymentRes, {
        'payment_success': (r) => r.status === 200,
    })
    paymentSuccessRate.add(paymentRate)
    paymentTime.add(paymentRes.timings.duration)


}