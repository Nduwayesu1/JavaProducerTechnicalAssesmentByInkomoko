# JavaProducerTechnicalAssesmentByInkomoko
Features
----------
Dual Data Source Integration: CRM customer data and inventory product data
Scheduled Processing: Automatic data fetching every 30 seconds
Fault Tolerance: Retry mechanisms with exponential backoff
Real-time Messaging: Kafka integration for event-driven architecture
Comprehensive Logging: Detailed operational insights
REST API Integration: Seamless connection to external systems

 Technology Stack
 ----------------
 Java: JDK 23.0.1
Spring Boot: 3.5.5
Spring Kafka: 3.3.9
Apache Kafka: 3.9.1
Spring Retry: 2.0.12
Jackson: 2.19.2
Tomcat: 10.1.44

Prerequisites
-------------
JDK Installation (Windows) - Download JDK 23.0.1
Install JDK
Set Environment Variables:
   Right-click "This PC" → Properties → Advanced system settings
   Environment Variables → System variables
   Add/Update:
   
  Verify Installation:
  -------------------
  java -version
  IDE Setup (IntelliJ IDEA Community Edition 2024.3)
  
 Configuratio
 -------------
 spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      group-id: integration-group
      auto-offset-reset: earliest

producer:
  schedule:
    enabled: true

app:
  crm:
    base-url: http://localhost:8080
  inventory:
    base-url: http://localhost:8081

logging:
  level:
    com.Inkomoko.Integration: DEBUG
    org.springframework.kafka: INFO
    
   Running the Application
   -----------------------
1.Open project in IntelliJ
2.Wait for Maven dependencies to download
3.Open IntegrationApplication.java
4.Click the green run button 
5. Then use this link: http://localhost:8080/swagger-ui/index.html  to test my API'S

API'S RESPOSNCE
---------------

<img width="925" height="389" alt="getAPI" src="https://github.com/user-attachments/assets/3ef90b1c-c19c-48f0-998e-e1a589d37fbf" />
<img width="916" height="369" alt="SOAP2" src="https://github.com/user-attachments/assets/8b1f0ca7-155d-4aaf-8297-79ee12943bd7" />
<img width="923" height="379" alt="Inventory" src="https://github.com/user-attachments/assets/2a16139f-4f8b-4301-b6ea-221dd819cba7" />
<img width="925" height="293" alt="crm" src="https://github.com/user-attachments/assets/deb2431a-cff9-4de7-ad3f-0d7abede01cd" />
<img width="929" height="371" alt="producer" src="https://github.com/user-attachments/assets/36da167e-cc6d-4458-8916-4b148626fe41" />
<img width="904" height="382" alt="Task2ScreenShootInkomoko" src="https://github.com/user-attachments/assets/35b612c2-1d22-4c32-9343-fbc25d7388be" />

 
