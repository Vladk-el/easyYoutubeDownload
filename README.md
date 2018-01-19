# EasyYoutubeDownload
Simple SpringBoot Java server & AngularJs client app to dl your favorite music.

## Prerequisites
- Java 8 or higher (sudo apt-get install default-jdk)
- Maven 3.3.x or higher
- Python 2.7 (sudo apt-get install python)
- ffmpeg ($ sudo apt-get install ffmpeg)

## Install
```
$ sudo wget https://yt-dl.org/downloads/latest/youtube-dl -O /usr/local/bin/youtube-dl
$ sudo chmod a+rx /usr/local/bin/youtube-dl
$ git clone https://github.com/Vladk-el/easyYoutubeDownload.git
$ cd easyYoutubeDownload
$ mvn clean install
```
If you want to use the default locations :
```
$ sudo mkdir /var/easy-youtube-download && sudo chown $USER. /var/easy-youtube-download
$ mkdir /var/easy-youtube-download/music && mkdir /var/easy-youtube-download/h2
$ sudo mkdir /var/log/easy-youtube-download && sudo chown $USER. /var/log/easy-youtube-download
```

## Options
- Server port (default 8082) : --server.port={your_wanted_port}
- Download path (default /var/easy-youtube-download/music)--download.base.path={your_wanted_download_path}
- Logs path (default /var/log/easy-youtube-download) : --logging.path={your_wanted_logging_path}
- H2 path (default /var/easy-youtube-download/h2) : --h2.path={your_wanted_h2_path}
- H2 console (default true) : --spring.h2.console.enabled={true/false}
- H2 username (default sa) : --spring.datasource.username={your_wanted_h2_username}
- H2 password (empty by default) : --spring.datasource.password={your_wanted_h2_password}

## Launch
```
$ java -jar target/easy-youtube-download-exec.jar [--option=value...] &
```

