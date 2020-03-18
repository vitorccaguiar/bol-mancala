FROM node:13.8-stretch-slim as node
COPY /package.json /usr/src/app/client/package.json
WORKDIR /usr/src/app/client/
RUN rm -rf node_modules && npm install
COPY ./ ./
ARG env=prod
RUN npm run build

FROM nginx:1.17
COPY --from=node /usr/src/app/client/dist/client /usr/share/nginx/html