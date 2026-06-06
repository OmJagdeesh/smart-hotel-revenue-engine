# StayWise

This is my hotel revenue management mini project. It has a Spring Boot backend and a React dashboard. The main idea is to store hotels, rooms and bookings, then calculate a room price using occupancy and a demand factor.

I kept the project as a simple full-stack app instead of making it a big production system.

## Tech Used

- Java 17 + Spring Boot
- Spring Security + JWT login
- Spring Data JPA
- MySQL
- React + Vite
- Recharts

## Folder Structure

```text
backend/
  src/main/java/com/staywise/hotel_revenue_engine/
    controller/       REST APIs
    service/          main business logic
    repository/       JPA repositories
    entity/           database tables
    dto/              request/response classes
    config/           Spring config and sample data
    security/         JWT related code

frontend/
  src/
    App.jsx
    components/
    services/
```

## How To Run

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`.

## Database

The backend expects MySQL on localhost. The current username/password is in:

```text
backend/src/main/resources/application.yml
```

By default it uses `staywise_db` and lets Hibernate update the tables.

## Login Details

- Admin: `admin@staywise.io` / `Admin@123`
- Manager: `manager@sunrise.com` / `Manager@123`

## Main Pricing Formula

The pricing code is in `PricingService`.

```text
price = base_price * demand_factor * (1 + occupancy_rate * 0.5)
```

Example: if the base price is 1000, demand factor is 1.2 and occupancy is 80%, the final price becomes 1680.

## APIs I Mostly Tested

Login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"manager@sunrise.com","password":"Manager@123"}'
```

Calculate price:

```bash
curl -X POST http://localhost:8080/api/pricing/calculate \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"hotelId":1,"roomId":1,"businessDate":"2026-04-20","demandFactor":1.2}'
```

Analytics:

```bash
curl "http://localhost:8080/api/analytics/hotel/1?startDate=2026-04-01&endDate=2026-04-30" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Tests

```bash
cd backend
mvn test
```

Right now I have tests for:

- price calculation
- double-booking check

## Things I Would Improve Later

- Add better validation messages on frontend
- Add booking create form in React
- Replace the hard-coded JWT text box with a real login page
- Add more test cases for roles and hotel access
