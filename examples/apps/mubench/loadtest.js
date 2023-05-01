import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 5,
  duration: '5m',
};

export default function () {
  http.get('http://172.28.0.3:31113/s0');
  http.get('http://172.28.0.4:31113/s0');
  http.get('http://172.28.0.5:31113/s0');
  http.get('http://172.28.0.6:31113/s0');
  sleep(1);
}