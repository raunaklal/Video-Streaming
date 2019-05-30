# Video-Streaming
In this project, the .mp4 video files get downloaded onto the client's machine from the server and starts playing.

Before running this application, one has to run the following commands to generate the list of the .mp4 video files from the server and save it in a menu.txt file.

For Server running on Windows,

To save the list of files we run the following command in the command prompt
    dir|findstr ".mp4"|gawk "{print $4}" > menu.txt
    
It may be $4 or $5 depending on your machine.

For Servver running on Linux, use the following command:
  
  ls | grep .mp4 > menu.txt

Then, run the Server program and ask the clients to run the client program. The client will receive a list of .mp4 videos from the server, after which the client will choose from them and respond to the server. Then the file will be transferred from the server to the client, and the video will start playing.

Thank You
