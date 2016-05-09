# Config Properties
 
## Description

Simplifie la configuration à partir d'un fichier interne ou externe de `properties` vers un Component Bean.

## Configuration

Ajouter dans le pom.xml :

```xml
<dependency>
	<groupId>com.synaptix</groupId>
	<artifactId>config-properties</artifactId>
	<version>1.2.0</version>
</dependency>
```

## Utilisation

Créer un objet component de configuration.

```java
@ComponentBean
public interface IConfig extends IComponent {

    String getNomadeSerlvetUrl();

    ServiceImplType getServiceImplType();

    long getMaxSizeUploadAvarieImage();

    Path getPublicAttachmentsDirectory();

    IMailConfig getMailConfig();

    List<String> getGroups();

    String[] getRoles();

    Set<String> getEnums();

    Map<ServiceImplType, Boolean> getBooleanMap();

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }
    
    @ComponentBean
    public interface IMailConfig extends IComponent {
    
        String getSmptHost();
    
        Integer getSmptPort();
    
    }
}
```

Créer la config :

```java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
        ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake));
builder.configProperty(ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));
builder.configProperty(ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")));
builder.configProperty(ConfigProperty.toString("server.mail.smtp.host", ConfigFields.mailConfig().dot().smptHost().name(), null),
        ConfigProperty.toInteger("server.mail.smtp.port", ConfigFields.mailConfig().dot().smptPort().name(), null));
builder.configProperty(CollectionConfigProperty.toList("server.groups", ConfigFields.groups, ConfigProperty.STRING_FROM_STRING, null),
        CollectionConfigProperty.toSet("server.enums", ConfigFields.enums, ConfigProperty.STRING_FROM_STRING, null));
builder.configProperty(ArrayConfigProperty.toArrayString("server.roles", ConfigFields.roles,  null));
builder.configProperty(MapConfigProperty.toHashMap("server.booleans", ConfigFields.booleanMap, IConfig.ServiceImplType::valueOf, ConfigProperty.BOOLEAN_FROM_STRING, null));
IConfig config = builder.build();
```

Par défault, il lit dans la variables système `config.file` pour trouver le chemin du fichier ou en interne dans `config.properties` à partir de la racine.

Les valeurs peuvent être changé :
 
``` java
builder.configLoader(DefaultConfigLoader.newBuilder().systemPropertyName("configuration").internalPropertiesPath("others/others.properties").build());
```

## Détails des types définits

| Type | IConfigProperty |
|---|------------|
| String | ConfigProperty.toString |
| Integer | ConfigProperty.toInteger |
| Long | ConfigProperty.toLong |
| Double | ConfigProperty.toDouble |
| Float | ConfigProperty.toFloat |
| Enum, ... | ConfigProperty.toGeneric |
| List<E> | CollectionConfigProperty.toList |
| Set<E> | CollectionConfigProperty.toSet |
| String[] | ArrayConfigProperty.toArrayString |
| Integer[] | ArrayConfigProperty.toArrayInteger |
| Long[] | ArrayConfigProperty.toArrayLong |
| Double[] | ArrayConfigProperty.toArrayDouble |
| Float[] | ArrayConfigProperty.toArrayFloat |
| Boolean[] | ArrayConfigProperty.toArrayBoolean |
| E[] | ArrayConfigProperty.toArrayGeneric |
| Map<E,F> | MapConfigProperty.toHashMap |
| Properties | PropertiesConfigProperty |

**Vous pouvez ajouter d'autres types en créant une class implementant un `IConfigProperty`**