# HttpSignatureUtility

Simple Kotlin utility for parsing a `Signature` header string into something more usable, validating HTTP signatures, and more.

## Installation

This library requires the use of a specific repository.

### Maven

```xml
<repositories>
    <!-- switch depending on if the version is snapshot or not -->
    <repository>
        <id>site.remlit.main-releases</id>
        <url>https://repo.remlit.site/releases</url>
    </repository>
    <repository>
        <id>site.remlit.main-snapshots</id>
        <url>https://repo.remlit.site/snapshots</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>site.remlit.blueb</groupId>
        <artifactId>httpSignatureUtility</artifactId>
        <version>${latest_version}</version>
    </dependency>
</dependencies>
```

### Gradle 

```kotlin
repositories {
    maven {
        // switch depending on if the version is snapshot or not
        url = uri("https://repo.remlit.site/releases")
        url = uri("https://repo.remlit.site/snapshots")
    }
}

dependencies {
    implementation("site.remlit.blueb", "httpSignatureUtility", "$latestVersion")
}
```

## Usage

[See a working usage](https://github.com/ihateblueb/aster-kt/blob/main/src/main/kotlin/service/ap/ApValidationService.kt)

### Parsing a `Signature` header

Parsing a Signature header lets you access the underlying key id, algorithm, important headers, and signature easily.

```kotlin
val sampleSignatureHeader = "keyId=\"https://activitypub.academy/users/bedacia_tukaban#main-key\",algorithm=\"rsa-sha256\",headers=\"(request-target) host date digest content-type\",signature=\"BAuQOg3NI6bf2cnx1PJaT+wvdh2qZcS5ZWi/Lbf15JJeBOzoypC0ZdNiE2BXKPPeuK2mbZF7ofscpbRqS7A9mdag/qvNl8z/CkSCyEbFOCm6wd1hlKsh8hXQQBCM2pb3yKhonEviqBbWt2S+bClS9JY5SDgexpdWGOFM6CUy0G7rvvt16mXAvpwh94NycnnssENxqGJCAIDsvo/b9ETjr6U7Vc9sCvoy7vMjmH9j3IdsAq55Xp3UbxggHHLHhtmHRH9VmI02FL9bc3QOBWHGlDNs0nx31IzTAJqGOLcnjkvjGTRx+k3jd4YDrwF0pmX5JM7w5vYJdCLsRoyuqHrXcA==\""
val parsedSignatureHeader = HttpSignature.parseHeaderString(sampleSignatureHeader)
val httpSignature = parsedSignatureHeader.signature // Returns Signature
```

### Validating a Signature

The Signature class has a method for making sure a signature is valid and trustworthy. 
This requires a date to be passed in order to make sure the signature was created within a reasonable time period of it being processed.

```kotlin
val isSignatureValid = try {
    parsedSignatureHeader.signature.verify(
        senderPublicKey,
        parseDateHowever(request.headers["Date"]),
        body
    )
} catch (e: SignatureException) {
    false
}
```