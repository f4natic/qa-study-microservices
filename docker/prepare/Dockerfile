FROM openjdk:17-alpine
LABEL authors="fnt"

# Установка git и maven
RUN apk update && apk add --no-cache git maven

# Создание директории для проекта
WORKDIR /app

# Клонирование проекта с GitHub
ARG REPO_URL
RUN git clone $REPO_URL

# Перемещение в директорию qa-study-microservices
WORKDIR /app/qa-study-microservices

# Смена ветки на develop
RUN git checkout develop

# Определение точки входа
CMD ["mvn", "clean", "package"]