import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate } from 'k6/metrics';
import {randomIntBetween, randomItem} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

let pointSearchTime = new Trend('point_search_time');
let pointSearchRate = new Rate('point_search_success_rate')
let pointChargeRate = new Rate('point_charge_success_rate')
let pointChargeTime = new Trend('point_charge_time');

export const options = {
    stages: [
        { duration: '1m', target: 100 }, // ramp-up to 100 users
        { duration: '3m', target: 300 }, // stay at 100 users
        { duration: '1m', target: 100 }, // ramp-up to 100 users
    ],
};
export default function () {
    let userId = randomIntBetween(1, 50);
    const payload = JSON.stringify({
        //1부터 50까지 random
        userId : userId,
    })

    const params = {
        headers : {
            'Content-Type' : 'application/json',
            tags: {name: 'search-point'}

        }
    }

    //포인트 조회
    let searchRes = http.post("http://localhost:8080/api/points", payload, params)
    let searchSuccess = check(searchRes, {
        'point_search_success': (r) => r.status === 200,
    })

    pointSearchRate.add(searchSuccess)
    pointSearchTime.add(searchRes.timings.duration);

    //충전전 think time
    sleep(3);

    const pointChargePayload = JSON.stringify({
        userId : userId,
        amount : randomIntBetween(1000, 3000),
        tags: {name: 'charge-point'}
    })

    //포인트 충전
    let chargeRes = http.post("http://localhost:8080/api/points/charge", pointChargePayload, params)

    let chargeSuccess = check(chargeRes, {
        'point_charge_success': (r) => r.status === 200,
    })

    pointChargeRate.add(chargeSuccess)
    pointChargeTime.add(chargeRes.timings.duration);

}