# build stage
# install simple http server for serving static content
FROM node:lts-alpine AS build-stage
# make the 'app' folder the current working directory
WORKDIR /app
# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . ./
# install project dependencies
RUN yarn install
# build app for production with minification
RUN yarn generate
ARG BUILD_SCRIPT
RUN yarn $BUILD_SCRIPT

# production stage
FROM node:lts-alpine AS production-stage
# make the 'app' folder the current working directory
WORKDIR /app
COPY --from=build-stage /app ./

ENV HOST 0.0.0.0
EXPOSE 80 443

CMD [ "yarn", "start" ]