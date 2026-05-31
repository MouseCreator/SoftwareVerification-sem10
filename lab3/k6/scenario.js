import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    stages: [
        // Phase 1: users go from 1 to 100 during 1 minute
        { duration: '1m', target: 100 },

        // Phase 2: 1000 users work together for 3 minutes
        { duration: '3m', target: 100 },

        // Phase 3: users go from 100 to 1 during 1 minute
        { duration: '1m', target: 1 },
    ],

    thresholds: {
        http_req_failed: ['rate<0.05'],
        http_req_duration: ['p(95)<1000'],
    },
};

const BASE_URL = 'http://localhost:8080';

const AUTHORS = [];

for (let i = 1; i <= 20; i++) {
    AUTHORS.push(`author${i}`);
}

const JSON_HEADERS = {
    headers: {
        'Content-Type': 'application/json',
    },
};

export default function () {
    const number = `${__VU}-${__ITER}-${Date.now()}`;
    const author = AUTHORS[randomIntBetween(0, AUTHORS.length - 1)];

    /**
     * 1. Create book
     *
     * BookCreateDTO:
     * {
     *   title: String,
     *   author: String
     * }
     */
    const createPayload = JSON.stringify({
        title: `Title ${number}`,
        author: author,
    });

    const createRes = http.post(`${BASE_URL}/books`, createPayload, JSON_HEADERS);

    const createOk = check(createRes, {
        'create book status is 200 or 201': (r) => r.status === 200 || r.status === 201,
        'create book response has id': (r) => r.json('id') !== undefined && r.json('id') !== null,
    });

    if (!createOk) {
        sleep(5);
        return;
    }

    const book = createRes.json();
    const bookId = book.id;

    /**
     * 2. Get this book by id
     */
    const getByIdRes = http.get(`${BASE_URL}/books/${bookId}`);

    check(getByIdRes, {
        'get book by id status is 200': (r) => r.status === 200,
        'get book by id returns correct id': (r) => Number(r.json('id')) === Number(bookId),
    });

    sleep(1);

    /**
     * 3. Update book
     *
     * BookUpdateDTO:
     * {
     *   id: Long,
     *   title: String,
     *   author: String
     * }
     */
    const updatePayload = JSON.stringify({
        id: bookId,
        title: `Title ${number} updated`,
        author: author,
    });

    const updateRes = http.put(`${BASE_URL}/books`, updatePayload, JSON_HEADERS);

    check(updateRes, {
        'update book status is 200': (r) => r.status === 200,
        'update book title is updated': (r) => r.json('title') === `Title ${number} updated`,
    });

    /**
     * 4. Get all books by this author
     *
     * Expected endpoint:
     * GET /books?author=author1
     */
    const encodedAuthor = encodeURIComponent(author);
    const getByAuthorRes = http.get(`${BASE_URL}/books?author=${encodedAuthor}`);

    check(getByAuthorRes, {
        'get books by author status is 200': (r) => r.status === 200,
        'get books by author returns array': (r) => Array.isArray(r.json()),
    });

    sleep(1);

    /**
     * 5. Delete book
     */
    const deleteRes = http.del(`${BASE_URL}/books/${bookId}`);

    check(deleteRes, {
        'delete book status is 200, 202, or 204': (r) =>
            r.status === 200 || r.status === 202 || r.status === 204,
    });

    /**
     * 6. Get all books
     */
    const getAllRes = http.get(`${BASE_URL}/books`);

    check(getAllRes, {
        'get all books status is 200': (r) => r.status === 200,
        'get all books returns array': (r) => Array.isArray(r.json()),
    });

    /**
     * 7. Wait 5 seconds
     */
    sleep(5);
}