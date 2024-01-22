## Project Overview
QR Code Service is a Java-based RESTful web application built with Spring Boot. It offers an API for generating custom QR codes, allowing users to specify various parameters such as content, size, image type, and error correction level. The service is designed to be versatile, user-friendly, and easily integrable into various applications requiring QR code functionality.

### Key Features
- **Health Check Endpoint:** A simple endpoint to check the operational status of the service.
- **Dynamic QR Code Generation:** Generate QR codes with customizable content, size, format (PNG, JPEG, GIF), and error correction levels (Low, Medium, Quartile, High).

### Technologies
- Java
- Spring Boot
- ZXing Library
- REST API

### Prerequisites
- Java JDK 8 or later
- Maven or Gradle (for dependency management and running the project)

## Usage
The service exposes two main endpoints:

1. **Health Check:**
   - Endpoint: `GET /api/health`
   - Description: Returns the status of the service.
   - Response: `200 OK`

2. **QR Code Generation:**
   - Endpoint: `GET /api/qrcode`
   - Parameters:
     - `contents`: The content to encode in the QR code (String, mandatory)
     - `size`: The size of the QR code (integer, optional, default 250)
     - `type`: The image type (png, jpeg, gif; optional, default png)
     - `correction`: Error correction level (L, M, Q, H; optional, default L)
   - Description: Generates a QR code based on the provided parameters.
   - Response: QR code image in the specified format
