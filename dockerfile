FROM node:latest As build-node
WORKDIR /app
COPY . ./
RUN yarn install
RUN yarn build

FROM nginx:alpine As depolyed
COPY --from=build-node /app/build/ /usr/share/nginx/html
