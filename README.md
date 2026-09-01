# 🚀 CareerPilot AI

> An AI-powered career preparation backend that analyzes resumes, identifies skill gaps, generates personalized career roadmaps, provides RAG-based technical assistance, and conducts AI-powered mock interviews.

## 📌 Overview

CareerPilot AI is a Spring Boot backend designed to provide an end-to-end AI career preparation platform.

The system takes a user's **resume, current skills, and target job role** and uses AI to analyze their profile, identify missing skills, generate a personalized learning roadmap, answer technical questions using RAG, and evaluate their performance through mock interviews.

### Complete Flow

```text
Resume
   +
Current Skills
   +
Target Job Role
        │
        ▼
┌─────────────────────┐
│    CareerPilot AI   │
└──────────┬──────────┘
           │
     ┌─────┼──────┬──────────────┐
     ▼     ▼      ▼              ▼
  Resume  Skill  Roadmap       Mock
 Analysis  Gap   Generator    Interview
                    │
                    ▼
              RAG Knowledge
                Assistant
✨ Features
📄 Resume Analysis

Analyzes an uploaded resume using Google Gemini AI.

Provides:

Technical skills
Soft skills
Projects
Education
Experience
Strengths
Weaknesses
Resume improvement suggestions
🎯 Skill Gap Analysis

Compares the user's current skills with the requirements of their target job role.

Current Skills
      +
Target Job Role
      ↓
AI Analysis
      ↓
Required Skills
      ↓
Skill Gaps
      ↓
Priority Areas

Identifies:

Existing skills
Missing skills
Important skills
Skills to prioritize
Areas requiring improvement
🗺️ AI Career Roadmap

Generates a personalized 6-month learning roadmap based on:

Target job role
Current skills
Skill gaps
Experience level
Industry expectations

The generated roadmap contains:

Monthly learning topics
Skills to develop
Learning resources
Practical projects
Expected outcomes

The roadmap dynamically changes according to the user's target role.

🧠 RAG Knowledge Assistant

CareerPilot implements Retrieval-Augmented Generation (RAG) using Spring AI and PostgreSQL with PGVector.

Technical knowledge is stored as documents and converted into vector embeddings.

Document Ingestion
Knowledge Documents
        ↓
Document Loader
        ↓
Token Text Splitter
        ↓
Text Chunks
        ↓
Gemini Embedding Model
        ↓
768-Dimensional Embeddings
        ↓
PGVector
Question Answering
User Question
      ↓
Generate Query Embedding
      ↓
PGVector Similarity Search
      ↓
Retrieve Relevant Chunks
      ↓
Build Prompt With Context
      ↓
Gemini LLM
      ↓
AI Answer

The vector store uses:

PostgreSQL
PGVector
768-dimensional embeddings
HNSW index
Cosine similarity
🎤 AI Mock Interview

CareerPilot provides an interactive AI mock interview system.

Interview Flow
Target Job Role
      ↓
Create Interview Session
      ↓
Generate Interview Question
      ↓
Candidate Answer
      ↓
AI Evaluation
      ↓
Score + Feedback
      ↓
Next Question
      ↓
Final Interview Report

The system evaluates answers and generates:

Individual answer scores
Feedback
Strengths
Weaknesses
Recommended topics
Overall score
Final recommendation
🤖 AI Architecture

Different AI capabilities are used for different features.

                    Google Gemini
                         │
        ┌────────────────┼────────────────┐
        ▼                ▼                ▼
 Resume Analysis    Roadmap          Mock Interview
        │           Generation           │
        ▼                               ▼
 Skill Gap                         Answer Evaluation
        │
        ▼
 RAG Knowledge Assistant
        │
        ▼
     PGVector
🏗️ Backend Architecture
                    REST Clients
                         │
                         ▼
                ┌─────────────────┐
                │  Spring Boot API│
                └────────┬────────┘
                         │
       ┌─────────────────┼──────────────────┐
       │                 │                  │
       ▼                 ▼                  ▼
 Controllers          Services          Security
       │                 │                  │
       └─────────────────┼──────────────────┘
                         │
          ┌──────────────┼───────────────┐
          ▼              ▼               ▼
     PostgreSQL       PGVector       Gemini API
     Application      RAG Data       AI Processing
        Data
🔐 Authentication

CareerPilot uses Spring Security + JWT authentication.

Register
   ↓
Password Hashing
   ↓
Login
   ↓
Validate Credentials
   ↓
Generate JWT
   ↓
Client Sends JWT
   ↓
JWT Authentication Filter
   ↓
Protected API

Protected resources are associated with the authenticated user.

🗄️ Database

CareerPilot uses PostgreSQL for persistent application data.

Main application data includes:

User
 │
 ├── Resume
 │
 ├── Skill Gap
 │
 ├── Career Roadmap
 │
 └── Interview Sessions
          │
          └── Interview Questions
PGVector

The RAG vector store contains:

vector_store
 ├── id
 ├── content
 ├── metadata
 └── embedding vector(768)

An HNSW index with cosine similarity is used for efficient vector search.

🛠️ Tech Stack
Backend
Java 21
Spring Boot
Spring AI
Spring Security
JWT
Spring Data JPA
Hibernate
REST APIs
Maven
AI
Google Gemini
Gemini 2.5 Flash
Gemini Embedding Model
Prompt Engineering
Structured Output
Retrieval-Augmented Generation
Database
PostgreSQL
PGVector
JPA / Hibernate
DevOps
Docker
Docker Compose
Multi-stage Docker Build
📂 Project Structure
CareerPilot-AI/
│
├── src/
│   └── main/
│       ├── java/com/karthik/CareerPilot/AI/
│       │
│       │   ├── controllers/
│       │   ├── services/
│       │   ├── entities/
│       │   ├── repos/
│       │   ├── records/
│       │   ├── config/
│       │   └── security/
│       │
│       └── resources/
│           ├── application.yml
│           └── knowledge/
│               ├── docker.txt
│               ├── spring-boot.txt
│               └── system-design.txt
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
📡 API Overview
Authentication
POST /api/auth/register
POST /api/auth/login
Resume
POST /api/resume/analyze
Skill Gap
POST /api/skill-gap
Career Roadmap
POST /api/roadmap
RAG Knowledge Assistant
POST /api/knowledge/search
Mock Interview
POST /api/interviews
POST /api/interviews/{sessionId}/start
POST /api/interviews/{sessionId}/questions/{questionId}/answer
🐳 Docker Setup

CareerPilot is containerized using Docker.

The application runs with:

┌──────────────────────┐
│  CareerPilot Backend │
│     Spring Boot      │
│       :8080          │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ PostgreSQL + PGVector│
│       :5432          │
└──────────────────────┘

Start the complete backend:

docker compose up -d --build

Check containers:

docker ps

View backend logs:

docker compose logs -f careerpilot

Stop containers:

docker compose down
⚙️ Environment Variables

The Gemini API key is provided through an environment variable.

API_KEY=your_gemini_api_key

PostgreSQL configuration:

POSTGRES_DB=ragdb
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

Never commit API keys or .env files to GitHub.

👨‍💻 Author

Karthik

Computer Science Student
Java Backend & AI Developer

⭐ If you find CareerPilot AI useful, consider starring the repository.
