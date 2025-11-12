# Base image olarak Node kullanıp build ediyoruz
FROM node:18 AS builder

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Gerçek production image (Nginx ile serve ediyoruz)
FROM nginx:alpine
COPY --from=builder /app/dist/ /usr/share/nginx/html
