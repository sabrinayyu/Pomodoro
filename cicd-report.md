CI/CD is fully working for the backend. When we push a change to the project repository, our application is automatically built. If it builds successfully, the application gets rebuilt in a staging environment, and all our tests (around 700 of them) from 10/14 teams are run. If all the tests pass, we deploy the build to production.

Right now, our latest build is in production.