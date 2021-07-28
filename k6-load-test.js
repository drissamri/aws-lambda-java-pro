import http from 'k6/http';
import {check } from 'k6';

export let options = {
    stages: [
        { duration: "120s", target: 50 }, // simulate ramp-up of traffic
        { duration: "60s", target: 100 }, // stay at 10 users
        { duration: "120s", target: 50 }, // ramp-down to 0 users
    ]
};

export default () => {
    var url = 'https://a1fd3oa3h8.execute-api.eu-west-1.amazonaws.com/favorites';
    var payload = JSON.stringify({
        name: 'driss',
    });

    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let response = http.post(url, payload, params);
    check(response, {
        'is status 200': r => r.status === 200,
        'is id present': r => r.json().hasOwnProperty('id'),
        'is name driss': r => r.json().name === 'driss'
    });
}