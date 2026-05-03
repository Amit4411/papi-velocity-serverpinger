# ServerPinger - PlaceholderAPI Expansion

A PlaceholderAPI expansion for pinging Minecraft servers and displaying player information on your Velocity proxy.

## Features

✅ Ping multiple servers with IP:PORT format
✅ Get online player count
✅ Get maximum player count
✅ Display server MOTD
✅ Show server version
✅ Check server status (Online/Offline)
✅ 5-second caching to reduce server load
✅ Easy placeholder format

## Installation

1. **Build the project:**
```bash
git clone https://github.com/Amit4411/papi-velocity-serverpinger.git
cd papi-velocity-serverpinger
mvn clean package
```

2. **Get the JAR:**
   - Located at: `target/papi-velocity-serverpinger-1.0.0.jar`

3. **Install:**
   - Place JAR in your `plugins/` folder
   - Restart your server

## Usage

### Placeholder Format
```
%serverpinger_<IP>:<PORT>_<type>%
```

### Available Types

- `online` - Number of players online
- `max` - Maximum player capacity
- `motd` - Server message of the day
- `version` - Server version
- `status` - Online/Offline status

### Examples

```
# For a server at 192.168.1.100:25565
%serverpinger_192.168.1.100:25565_online%    → 25
%serverpinger_192.168.1.100:25565_max%       → 100
%serverpinger_192.168.1.100:25565_motd%      → Welcome to Server!
%serverpinger_192.168.1.100:25565_version%   → 1.20.1
%serverpinger_192.168.1.100:25565_status%    → Online

# For a second server at 192.168.1.101:25565
%serverpinger_192.168.1.101:25565_online%    → 45
```

## Requirements

- PlaceholderAPI installed
- Java 11 or higher
- Spigot/Paper server or Velocity proxy with PlaceholderAPI

## Performance

- Server pings are cached for 5 seconds
- Multiple requests within 5 seconds return cached data
- Prevents excessive ping requests to backend servers

## Author

- **Amit4411** - Creator

## Support

For issues or suggestions, open an issue on GitHub!
