# Back to Blackout
> Note that the code here is not the complete version of the project. The complete code is maintained on GitLab and could be published after 2025 due to UNSW policy. <br/>
Please contact me to review the complete version of the code in person.

[Checkout UML diagram of my implementation here](https://drive.google.com/file/d/1mV0JWhQIZe4L92LekX2HKLVFP1bh0QYj/view?usp=sharing)

## Table of Contents
1 [Preamble and Problem](#preamble-and-problem) </br>
2 [Requirements](#requirements) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.1 [Assumptions](#assumptions) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.2 [Devices](#devices) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.3 [Files](#files) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.4 [Satellites](#satellites) <br/>
3 [Demo](#demo) </br>
4 [Features](#features) 

## Preamble and Problem
Many of today's technology uses satellites that orbit the Earth in one way or another. For example, tagging photographs with their location, telecommunications, and even missile control systems. There are currently 4,550 active satellites (as of 2022) orbiting the Earth altogether.

In the far future, society has advanced to the point where they have begun to occupy a series of moons and asteroids around Jupiter. Individuals rely on satellites for all their communication desires. Three major satellites exist around Jupiter.

This assessment aims to provide you with design experience for a non-trivial system. You will architect and model how these multiple satellites communicate and function with various devices. The form of communication that you will be simulating is a simplified version of the modern internet, simply file transfer. You can either upload a file to a satellite from a device, download a file from a satellite to a device, or send a file from a satellite to a satellite.

## Requirements

1. Model the problem, including:
- Modeling the satellites/devices;
- Satellites/Devices must be able to be added/removed at runtime (with various consequences);
- Most importantly, write a series of 'queries' about the current world state, such as what devices currently exist.

2. Allow satellites/devices to send files to other satellites.
3. Implement moving devices.

### Assumptions
In this problem, we are going to have to make some assumptions. Let us assume that:

- Satellites move around at a constant linear velocity regardless of their distance from the planet (their angular velocity would change based on distance, though).

- The model is two-dimensional.

- Objects do not rotate on their axis, and simple planetary orbit is the only 'rotation' allowed in the system.

- Jupiter has a radius of `69,911` kilometers.

For the sake of consistency:

- All distances are in kilometers (`1 km = 1,000 m`).

- Angular velocity is in radians per minute (not per second).

- Linear velocity is in kilometers per minute (not per second).

### Devices
There are three types of devices available. Each device has a maximum range from which it can connect to satellites.

- `HandheldDevice` – phones, GPS devices, tablets.

  - Handhelds have a range of only 50,000 kilometers (50,000,000 meters)

- `LaptopDevice` – laptop computers.

  - Laptops have a range of only 100,000 kilometers (100,000,000 meters)

- `DesktopDevice` – desktop computers and servers.

  - Desktops have a range of only 200,000 kilometers (200,000,000 meters)

### Files
Devices can store an infinite number of files and can upload/download files from satellites. Files are represented simply by a string representing their content and a filename representing their name.

All files can be presumed to purely consist of alphanumeric characters or spaces (i.e., a-z, A-Z, 0-9, or spaces), and filenames can be presumed to be unique (i.e., we will never create two files of the same name with different content). Furthermore, since we are dealing with a simple subset, 1 character is equivalent to 1 byte. We will often refer to the size of files in terms of bytes, and the file size only relates to the file's content (not the filename).

To send files, the target needs to be within the range of the source BUT the source does not have to be within the target range. For example if a `HandheldDevice` (range `50,000 km`) is `100,000 km` away from a `StandardSatellite`(range `150,000 km`), it can't send files to the satellite but it can receive files from the satellite. If the device is `160,000 km` away from the satellite, neither can interact with the other. Satellites can also send files to other satellites, but devices can not send files to other devices.

Files do not send instantly, however, and are limited by the bandwidth of the satellites. Satellites will always ensure fairness and evenly allocate bandwidth to all currently uploading files (for example, if a satellite has a bandwidth of 10 bytes per minute and 3 files, every file will get 3 bytes per minute. You'll have 1 unused bandwidth). Devices aren't limited on how many downloads/uploads they can do.

If a device goes out of range of a satellite during the transfer of a file (either way), the partially downloaded file should be removed from the recipient. This does raise the question of whether or not you should start transferring a file if it's obvious that it won't finish. Solving this problem is mostly algorithmic and isn't particularly interesting to the point of this assignment so you don't have to do anything special here: if someone asks to transfer a file... begin to transfer it.

During a file transfer, when sending speed and receiving speed differ, the transfer rate is bottlenecked by `min(sending speed, receiving speed)`.

### Satellites
There are 2 specialised types of satellites (and one basic one). Satellites have a set amount of bandwidth for transferring files and a set amount of storage.

The default direction for all satellites is negative (clockwise), unless otherwise specified.

- `StandardSatellite`

  - Moves at a linear speed of 2,500 kilometres (2,500,000 metres) per minute

  - Supports handhelds and laptops only (along with other satellites)

  - Maximum range of 150,000 kilometres (150,000,000 metres)

  - Can store up to either 3 files or 80 bytes (whichever is smallest for the current situation).

  - Can receive 1 byte per minute and can send 1 byte per minute, meaning it can only transfer 1 file at a time.

- `TeleportingSatellite`

  - Moves at a linear velocity of 1,000 kilometres (1,000,000 metres) per minute

  - Supports all devices

  - Maximum range of 200,000 kilometres (200,000,000 metres)

  - Can receive 15 bytes per minute and can send 10 bytes per minute.

  - Can store up to 200 bytes and as many files as fit into that space.

  - When the position of the satellite reaches θ = 180, the satellite teleports to θ = 0 and changes direction.

  - If a file transfer from a satellite to a device or a satellite to another satellite is in progress when the satellite teleports, the rest of the file is instantly downloaded. However, all `"t"` letter bytes are removed from the remaining bytes to be sent.

    - For the satellite-to-satellite case, the behaviour is the same, whether the sender or receiving is teleporting.

  - If a file transfer from a device to a satellite is in progress when the satellite teleports, the download fails, the partially uploaded file is removed from the satellite, *and* all `"t"` letter bytes are removed from the file on the device.

  - There is no 'correction' with the position after a teleport occurs as there is for Relay Satellites (see below). Once the satellite teleports to θ = 0, it does not continue moving for the remainder of the tick.

  - Teleporting satellites start by moving anticlockwise.

- `RelaySatellite`

  - Moves at a linear velocity of 1,500 kilometres (1,500,000 metres) per minute

  - Supports all devices

  - Max range of 300,000 kilometres (300,000,000 metres)

  - Cannot store any files and has no bandwidth limits

  - Devices/Satellites cannot transfer files directly to a relay but instead a relay can be automatically used by satellites/devices to send to their real target.

    - For example, if a `HandheldDevice` (range `50,000km`) is `200,000km` away from a `StandardSatellite` that it wishes to communicate with, it can communicate to the satellite through the use of the relay if the relay is within `50,000km` of the device. The satellite is within `300,000km` (the range of the relay) of the relay.

    - Files being transferred through a relay should not appear in the relay's list of files.

  - Only travels in the region between `140°` and `190°`

    - When it reaches one side of the region its direction reverses, and it travels in the opposite direction.

      - This 'correction' will only apply at the next minute. This means that it can briefly exceed this boundary. There is a unit test that details this behaviour quite well called `testRelaySatelliteMovement` in `Task2ExampleTests.java`

    - You can do the radian maths here yourself or use the functions in `src/unsw/utils/Angle.java` to make comparisons.

    - In the case that the satellite doesn't start in the region `[140°, 190°]`, it should choose whatever direction gets it to the region `[140°, 190°]` in the shortest amount of time.

    - Relay satellites don't allow you to ignore satellite requirements (other than visibility/range). For example, you can't send a file from a Desktop Device to a Standard Satellite because a Standard Satellite doesn't support Desktops. This should hold *even if* a Relay is used along the way.

## Demo

https://github.com/PhotKosee/blackout/assets/114990364/6a29cc95-0264-43b3-89e4-52ab510c2066

## Features
This project mainly focuses on defining a relationship between entities and applying design patterns.

- Maintained good quality of code by implementing a pipeline for testing before refactoring the design
- Applied strategy pattern for entities with similar behaviors

![blackout_show](https://github.com/PhotKosee/blackout/assets/114990364/ac7f81ad-6acb-4a50-8794-6eca01be8abf)
