## Personal Finance Tracker (MVP - Phase 0)

The personal finance tracker will be used to track user spending.

1. **Goal & Scope**

   - **In Scope**

     - Account creation.
     - Submit basic transactions of type _Credit_ and _Debit_.
     - Retrieve the current balance for the user.
     - Use spring boot to implement REST APIs for this phase.
     - Use H2 in-memory database for storage
     - Think ahead with regards to scalability. Expected high volume of users even for MVP.
     - Avoid full history scans for every balance request, think about caching.

   - **Out of Scope**

     - Authentication.

2. **Context & Constraints**

   - Unique email address for account creation.
   - Negative balances are not allowed.
   - Handle Concurrency safely

3. **High-Level Architecture**

   - Use hexagonal architecure along with DDD, along with TDD.
   - Main layers of the Architecture

     - **Domain**
     - **Application**
     - **Infratructure**

   - Architecture Diagram

   ```mermaid
   graph TD
   subgraph Core ["Application Core (Hexagon)"]
       Domain[Domain Logic]
   end

   %% Define Adapters
   InputAdapter[("Input Adapter (REST API) Infrastructure (Spring-Web)")]
   OutputAdapter[("Output Adapter H2/Spring Events")]

   %% Connections
   InputAdapter --> Core
   Core --> OutputAdapter

   %% Stylings
   classDef hex fill:#ff,stroke:#333,stroke-width:2px;
   class Core,InputAdapter,OutputAdapter hex;

   ```

4. **Core Domain Concepts**

   - Account -> Aggregate root, prevents negative balance
   - Transaction -> Append only ledger. 2 types Credit and Debit
   - Money -> Respresents anounts/balance fields and their currency
   - Email -> Unique identifier for user/account

5. **Major Flows**

   - _Account Creation:_

     - REST POST request contains email and initial balance
     - REST layer calls application port, which validates and creates the domain Account using the factory
     - Application layer persists to db using db infrastructure port

   - _Post Transaction:_

     - REST POST request contains the transaction type and amount (with currency)
     - REST layer calls the application layer port which validates and creates the transaction using domain factory
     - Application layer persists the transaction using db infrastructure port

   - _Get Balance:_
     - REST layer receives balance request
     - Calls the application layer, which uses the db infrastructure port to retrieve transaction information
     - Domain layer computes the current balance

6. **Key Design Decisions & Trade-offs**

   - Balance storage:

     - Chosen approach: Calculated for each balance request using transactions. Can be a performed via an aggregate query
     - Alternatives: Caching, balance is updated in cache and then read from cache
     - Why ?: For mvp, caching introduces complexity in code and Infrastructure

   - Concurrency:

     - Chosen approach: Use versioning provided by JPA which implements optimistic locking.
     - Alternatives: Pessimistic locking (e.g. distributed lock)
     - Why ?: Pessimistic locking introduces code complexity, optimistic locking via versioning is a quick out of the box solution.

   - Transaction Identity:

     - Chosen approach: UUID
     - Alternatives: ?.
     - Why ? : In preparation for a distributed db, UUIDs offer less complexity

7. **Out-of-Scope / Future Work**

   - Authentication
   - Categories / tags
   - Recurring transactions
   - Reporting / charts

8. **Data Model**

   **Account**

   - id: UUID PK
   - email: string UNIQUE
   - currency: string (e.g. 'USD')
   - initial_balance: decimal
   - version: long (optimistic lock)
   - created_at: timestamp

   **Transaction**

   - id: UUID PK
   - account_id: FK
   - type: 'CREDIT' | 'DEBIT'
   - amount: decimal
   - created_at: timestamp
   - description: string nullable

9. **Future work & considerations**

   - Consider various composite indexes for the transactions table for future reporting functionality
