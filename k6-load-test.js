import http from 'k6/http';
import {check } from 'k6';

export let options = {
    stages: [
        { duration: "10s", target: 20 }, // simulate ramp-up of traffic
        { duration: "10s", target: 150 }, // stay at 10 users
        { duration: "10s", target: 25 }, // ramp-down to 0 users
    ]
};

export default () => {
    var url = 'https://sc9ghyme32.execute-api.eu-west-1.amazonaws.com/Prod/favorites';
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