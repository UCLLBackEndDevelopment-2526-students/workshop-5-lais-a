# Back-End Development

# Workshop 5: Testing with a Database, One-To-One Relationship

**Authors:** N. Jacobs, J. Pieck, E. Steegmans, B. Van Impe, S. Van Peborgh, V. Witters

---

## Contents

1. [Introduction](#1-introduction)
2. [Getting started](#2-getting-started)
3. [Testing with a database](#3-testing-with-a-database)
   - [UserRepositoryStub](#31-userrepositorystub)
   - [User component test](#32-user-component-test)
4. [Java Persistence API: one-to-one](#4-java-persistence-api-one-to-one)
   - [One-to-one relationships](#41-one-to-one-relationships)
5. [Additional Practice](#5-additional-practice)

---

## 1. Introduction

We will continue from where the previous workshop left off: by re-adding our tests and refactoring them to make sure that they still work now that we use a database and JPA.

Afterwards we will code a `OneToOne` relationship between `User` and `Profile` and expand the `addUser` functionality so that it can create a `User` and a `Profile` at the same time.

We will finish by coding two “get” endpoints that need queries across the two relationship tables.

The concepts covered include:

- Testing
  - Creating a “stub” class and using it in Unit Tests
  - Changing the Component Test setup to reset the state of the database in-between tests
- JPA
  - Coding a `OneToOne` Relationship
  - Writing custom query methods to execute queries on the database across a relationship

You should be able to complete this workshop with the theory slides from class.

---

## 2. Getting started

To begin this workshop, please accept the following GitHub Classroom assignment:

<https://classroom.github.com/a/vweFsfi8>

As team name, use your full name.

Do not continue working on your project from Workshop 4. To ensure everyone starts with a correctly implemented foundation, we have provided a new project that contains the full solution to the previous workshop.

On Toledo, you will find multiple User Stories that describe the additional functionality we need to implement. Only open and read these files when instructed by this document. We recommend committing and pushing to your GitHub repository after every story.

Open the start code in IntelliJ to get started.

---

## 3. Testing with a database

We will start by refactoring the tests. Currently the application does not start because the tests do not compile anymore after changing the `UserRepository` to a `JpaRepository`.

### 3.1. UserRepositoryStub

It is important that our unit tests do not interact with a real database. Recall that our service unit tests are written using data sourced from the `UserRepository`. Since we have updated this repository to target a real database, we will implement a mock version for unit testing purposes that continues to utilize an in-memory `ArrayList`.

In the previous workshop, you created a backup of your old repository logic called `UserRepositoryList`. However, because it does not implement our new `UserRepository` interface, we cannot pass it to our `UserService` during unit testing. We need to create a proper “Stub”.

#### Step 1: Create the Stub class

Create a new package `src/test/be/ucll/unit/repository` and add a new class named `UserRepositoryStub`. Make it implement your new interface:

```java
public class UserRepositoryStub implements UserRepository {
}
```

#### Step 2: Generate missing methods

Because `UserRepositoryStub` implements `UserRepository` (which extends `JpaRepository`), Java requires you to provide an implementation for every single method defined in those interfaces. Hover over the red error on `UserRepositoryStub`, hit `Alt+Enter`, and select **Implement methods**. Select all methods in the list and click **OK**.

IntelliJ will generate dozens of methods that look like this:

```java
@Override
public void flush() {
    throw new UnsupportedOperationException("Unimplemented method 'flush'");
}
```

Leave all these exceptions as they are! We only need to provide real logic for the methods our `UserService` actually uses.

#### Step 3: Migrate the old logic to the new methods

Open your `UserRepositoryList` backup from the main folder. You cannot simply copy-paste the old methods (like `usersOlderThan` or `userExists`) because our new interface uses JPA standard names (like `findByAgeGreaterThan` and `existsByEmail`).

Instead, do the following:

1. Copy the `ArrayList<User> users` and the constructor containing the dummy data and paste them at the very top of your `UserRepositoryStub`.
2. Copy the `setUsers()` method.
3. Look for the empty JPA methods in your Stub that IntelliJ just generated (e.g., `findAll()`, `save()`, `existsByEmail()`, `findByAgeGreaterThan()`). You can move these to the top of the class to keep things organized.
4. Copy the internal logic from your old `UserRepositoryList` methods and paste them inside the corresponding new JPA methods in the Stub.

For example, replace the `UnsupportedOperationException` in the generated `findByAgeGreaterThan` method with the logic from your old `usersOlderThan` method:

```java
public class UserRepositoryStub implements UserRepository {

    public List<User> users;

    public UserRepositoryStub() {
        users = new ArrayList<>(...);
    }

    @Override
    public List<User> findByAgeGreaterThan(int age) {
        return users.stream().filter(user -> user.getAge() > age).toList();
    }
}
```

You can remove your `UserRepositoryList` class after all logic has been moved to the stub.

#### Step 4: Update the Unit Tests

Open `UserServiceTest`. Change the initialization of the repository to use your new stub:

```java
private UserService userService;
private UserRepositoryStub userRepository;

@BeforeEach
public void setUp() {
    userRepository = new UserRepositoryStub();
    userService = new UserService(userRepository, new LoanRepository());
}
```

Fix the compilation errors by calling the right methods from your stub. Run your `UserServiceTest` to verify that all unit tests pass again.

You need to do the exact same thing in your `LoanServiceTest`.

### 3.2. User component test

Our `UserTest` in the component package boots up the entire Spring application, which means it now connects to the real H2 database instead of an in-memory list.

#### Step 1: Remove old reset logic

Some of our component tests add, modify, or delete data. Previously, we called a manual `resetRepositoryData()` method in an `@AfterEach` or `@BeforeEach` block to clean up the list. Because we are working with a real database now, you can completely delete these manual reset methods from your test class.

#### Step 2: Reset the database with `@Sql`

To ensure every test runs in a clean state, we must tell Spring to drop and recreate the database tables before each test. We can do this by adding the `@Sql` annotation at the very top of the test class, pointing to the `schema.sql` file we created earlier.

```java
@SpringBootTest(...)
@AutoConfigureWebTestClient
@Sql("classpath:schema.sql")
public class UserTest {
    ...
}
```

#### Step 3: Pre-populate with DbInitializer

Because the `@Sql` script clears the database, the tables will be empty. We must re-populate the database before each test using our `DbInitializer`.

```java
@SpringBootTest(...)
@AutoConfigureWebTestClient
@Sql("classpath:schema.sql")
public class UserTest {
    @Autowired
    private DbInitializer dbInitializer;

    @BeforeEach
    public void setup() {
        dbInitializer.initialize();
    }

    ...
}
```

Run the `UserTest` class. All tests should now pass against your H2 database.

---

## 4. Java Persistence API: one-to-one

### 4.1. One-to-one relationships

First, read the story below.

| Story | Title |
|---|---|
| Story 01 | Add Profile to User |

Try to come up with a plan for implementing this functionality and code it. You can reference the steps below afterwards for an exhaustive overview of all necessary steps.

#### Step 1: Add a Profile class in the domain model

Most of the class is straightforward. All “non-empty” validations can be coded with Input Validation. Each of these three validations should get a test case in a new `ProfileTest` file.

#### Step 2: Code the relationship User - Profile

We need to make two choices: who is the owner of the relationship and will we make it uni- or bidirectional? For directionality, it is often a good idea to start with unidirectional relationships and only make them bidirectional if needed. It we start with a unidirectional relationship, then it makes sense to code it from `User` to `Profile`. Otherwise, we cannot implement the endpoint as requested.

Let’s add the relationship to `User`:

- Add a field `profile` with annotations `@OneToOne` and `@JoinColumn`.
  - Choose a column name for the foreign key and add it to the `@JoinColumn` annotation.
- Add a getter and setter for `profile`. Code the validation that only an adult can have a `Profile` in `setProfile`.
- Add a new constructor with 5 arguments: `name`, `age`, `email`, `password` and `Profile`.
  - You could also expand the current constructor, but this will lead to nasty code in all cases where a `User` is created without a `Profile`.

#### Step 3: Extend the Schema

Extend `schema.sql` with `DROP` and `CREATE` statements for a `profile` table. Expand the `user` table with a new column that is a foreign key to `profile`.

Extend `DBInitializer` so that some profiles are created on startup:

```java
@PostConstruct
public void initialize() {
    Profile profileJohn = profileRepository.save(new Profile("Student",
            "Antwerp", "Amazing science"));
    Profile profileJane = profileRepository.save(new Profile("Architect",
            "Leuven", "Sleeping"));
    Profile profileBirgit = profileRepository.save(new Profile("Java Programmer",
            "Hasselt", "Walking in the forest, Science"));

    User userJohn = new User("John Doe", 25, "john.doe@ucll.be",
            "john1234", profileJohn);
    userRepository.save(userJohn);

    User userJane = new User("Jane Toe", 30, "jane.toe@ucll.be",
            "jane1234", profileJane);
    userRepository.save(userJane);

    User user = new User("Birgit Doe", 18, "birgit.doe@ucll.be",
            "birgit1234", profileBirgit);
    userRepository.save(user);

    user = new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234");
    userRepository.save(user);

    user = new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234");
    userRepository.save(user);
}
```

`DbInitializer` will need a `ProfileRepository` to save the Profiles. Create that class now. It should be a standard `JpaRepository`.

#### Step 4: Code the endpoint

The `getMapping` in `UserRestController` is straightforward.

In the `UserService`, extend the `addUser` method as follows:

```java
public User addUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new RuntimeException("User already exists.");
    }

    if (user.getProfile() != null) {
        Profile profile = profileRepository.save(user.getProfile());
        user.setProfile(profile);
    }

    return userRepository.save(user);
}
```

In the implementation above, we follow the recipe from the slides. First we save the non-owning side (`profile`), then we connect the model and save the owning-side. If the request would try to create an underage `User` with a `Profile` then `setProfile` would be triggered during deserialization, leading to an Exception before reaching this piece of code.

The `UserRepository` and `ProfileRepository` need no changes.

#### Step 5: Tests

You should already have a component test where a user is added without a profile. Add a second one where a user and profile are created together.

The `UserServiceTest` also needs a change:

- As the `UserService` now depends on the `ProfileRepository`, we will need a `ProfileRepositoryStub` for the tests. Create that class now, it only needs to implement the `save` method. All other methods can throw an `UnsupportedOperationException`.

That should do it!

---

## 5. Additional Practice

To finish, you can try these two stories that ask to create endpoints that need queries that span the `User` and `Profile` class:

| Story | Title |
|---|---|
| Story 02 | Retrieve Users with specific interests. |
| Story 03 | Retrieve Users older than, with specific interests, sorted by location. |
