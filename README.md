# FraudGuard-Agentic-AI – AI-Powered Real-Time Fraud Transaction Detection & Investigation Platform

## Overview

FraudGuard is a real-time fraud detection and investigation platform built. The system detects suspicious banking transactions, generates fraud alerts, and automatically performs AI-driven investigations using Agentic AI to generate a report.

The platform combines:

- Spring Boot services
- Apache Kafka Event Streaming
- React Frontend
- JWT Authentication & Authorization
- LangGraph Agent Workflows
- MCP (Model Context Protocol) Tools
- OPenAI LLM model
- Automated AI Investigation Reports

Unlike traditional fraud detection systems that only flag suspicious transactions, FraudGuard automatically investigates fraud alerts and generates detailed forensic reports with recommendations.

---

# Features

## Authentication & Authorization

- JWT Based Authentication
- Role Based Access Control (RBAC)
- USER Role
- ADMIN Role

## Transaction Processing

- Create transactions
- Real-time transaction validation
- Event-driven architecture
- Kafka producers and consumers

## Fraud Detection Engine

Detects suspicious transactions using rule-based fraud detection.

Examples:

- Extremely large transfers
- Repeated high-value transactions
- Suspicious transfer patterns
- High-risk account activity

## Real-Time Alerts

When fraud is detected:

- Alert is created automatically
- Stored in database
- Sent to Admin Dashboard via WebSocket

## AI-Powered Fraud Investigation

Once a fraud alert is generated:

- Fraud Automator is triggered through Kafka
- Investigation Agent collects evidence
- LLM performs analysis
- Report is automatically generated

---

# MCP Tools

The agent uses MCP (Model Context Protocol) tools to gather evidence.

### Alert Tool

- Retrieves alert details
- Retrieves alert status
- Retrieves alert metadata

### Transaction Tool

- Retrieves transaction details
- Retrieves sender information
- Retrieves receiver information
- Retrieves transaction amount

### History Tool

- Retrieves historical transactions
- Retrieves fraud patterns
- Retrieves previous alerts

### Account Risk Tool

- Retrieves account risk score
- Retrieves behavioral risk indicators

---

# Tech Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL

## Event Streaming

- Apache Kafka
- Kafka Producers
- Kafka Consumers

## Frontend

- React
- Tailwind CSS
- Redux Toolkit
- Axios
- STOMP WebSockets
- SockJS

## AI Layer

- Python
- LangGraph
- MCP
- OpenAI
- ReportLab

## Infrastructure

- Docker
- Kafka Containers
- REST APIs
- Environment Variables

---

## ▶️ Setup & Run

### 1️⃣ Clone the repo

```bash
git clone https://github.com/TusharRanjan2401/fraudGuard
cd FraudGuard
```

---

### 2️⃣ Backend Setup (Spring Boot)

1. Go to `FruadGuard/`

   ```bash
   cd FruadGuard
   ```

2. Configure **application.properties** with your MySQL & Kafka details:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/fraudguard_db
       username: <your-username>
       password: <your-password>
     jpa:
       hibernate:
         ddl-auto: update

   kafka:
     bootstrap-servers: <server-ip>
   ```

3. Run the backend:
   ```bash
   mvn spring-boot:run
   ```

---

### 3️⃣ Frontend Setup (React)

1. Go to `fraud-guard-client/`

   ```bash
   cd fraud-guard-client
   ```

2. Install dependencies

   ```bash
   npm install
   ```

3. Start React app
   ```bash
   npm start
   ```

---

### 5️⃣ MySQL Setup

```sql
CREATE DATABASE fraudguard_db;
```

---

## 🔒 Security

- JWT-based authentication
- Role-based access control (Admin/User)
- Transactions restricted per user

---

## Author

@TusharRanjan[https://github.com/TusharRanjan2401/fraudGuard]
"# fraudGuard-Agentic-AI" 
