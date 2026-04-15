# StayWise - Hotel Revenue Management System

Production-grade full-stack template for multi-tenant hotel revenue optimization.

## Architecture
- Backend: Java Spring Boot (Maven)
- Database: MySQL + JPA/Hibernate
- Auth: JWT
- Frontend: React + Recharts
- Pattern: Controller -> Service -> Repository

## Project Structure

```text
smart-hotel-revenue-engine/
  backend/
    src/main/java/com/staywise/hotel_revenue_engine/
      controller/
      service/
      repository/
      entity/
      dto/
      config/
      security/
      exception/
    src/main/resources/
      application.yml
      schema.sql
  frontend/
    src/
      components/
      services/
```

## Multi-Tenant Model
- Each `Hotel` is a tenant.
- Tenant isolation is enforced by `hotel_id` foreign key in all major business tables (`rooms`, `bookings`, `pricing_rules`, `revenue_records`, hotel users in `users`).

## Backend Setup
1. Create MySQL DB user and ensure MySQL is running.
2. Update `backend/src/main/resources/application.yml` datasource credentials if needed.
3. Run backend:

```bash
cd backend
mvn spring-boot:run
```

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Default URL: `http://localhost:5173`

## API Docs (Swagger)
- UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Seed Credentials
- Admin: `admin@staywise.io` / `Admin@123`
- Hotel Manager: `manager@sunrise.com` / `Manager@123`

## Sample cURL Requests

### 1. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"manager@sunrise.com","password":"Manager@123"}'
```

### 2. Register Staff (tenant user)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"staff1@sunrise.com",
    "password":"Staff@1234",
    "fullName":"Front Desk Staff",
    "role":"STAFF",
    "hotelId":1
  }'
```

### 3. Create Booking (replace JWT)
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "hotelId":1,
    "roomId":1,
    "userId":2,
    "checkInDate":"2026-04-20",
    "checkOutDate":"2026-04-22",
    "demandFactor":1.15
  }'
```

### 4. Calculate Dynamic Price
```bash
curl -X POST http://localhost:8080/api/pricing/calculate \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "hotelId":1,
    "roomId":1,
    "businessDate":"2026-04-20",
    "demandFactor":1.2
  }'
```

### 5. Analytics
```bash
curl "http://localhost:8080/api/analytics/hotel/1?startDate=2026-04-01&endDate=2026-04-30" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Core Pricing Logic
Implemented in `PricingServiceImpl`:

```text
price = base_price * demand_factor * (1 + occupancy_rate * 0.5)
```

This increases price as occupancy grows and adjusts for demand seasonality.

## Scheduled Job
- Daily metrics refresh runs at 1:00 AM server time.
- Stores daily `RevenueRecord` with revenue, occupancy and RevPAR.

## Testing
Run unit tests:

```bash
cd backend
mvn test
```

Includes:
- Pricing engine formula test
- Double-booking prevention test
