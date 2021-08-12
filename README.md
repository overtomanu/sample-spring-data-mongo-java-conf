# sample-spring-data-mongo-java-conf
sample spring data mongo project with CRUD operations

- Uses webjars to get jquery and bootstrap js files
- Fallback to local js files coming from webjar if js file is not available from cdn
- Entity has version column to facilitate optimistic locking. 
    * Throw optimistic lock exception if entity object is updated by another user.
- Entity is auditable (has columns createdBy, creationDate, modifiedBy, modificationDate)
- Authentication based on users fetched from mongo db collection
- Uses DelegatingPasswordEncoder to support passwords encoded in multiple formats
- Loads initial authentication seed data on application startup
- Spring MVC conversational support - Can update multiple entity objects in multiple browser tabs in same session
- Entity has UUID id field generated.
- Doesn't use any library from spring boot, only uses spring framework
- Has integration tests for data repositories using in memory db and unit tests for controllers
- Has REST service exposed which is accessible to only user with ADMIN role
