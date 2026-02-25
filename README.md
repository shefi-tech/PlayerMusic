# PlayerMusic
1.Musical Player – Playback Module

Overview

The PlaybackActivity is the core screen of the Musical Player application. It is responsible for:

· Loading and playing an MP3 audio file · Displaying track metadata (title, artist, album art) · Managing playback controls (Play/Pause, Seek) · Rendering a waveform visualization · Applying Equalizer, Bass, and Treble effects · Syncing UI with playback progress

Architecture

The playback module follows a simplified MVVM architecture:

· PlaybackActivity → UI layer · PlaybackViewModel → Business logic layer · MediaPlayerManager → Audio engine controller · EqualizerStorage → Persistent audio settings · WaveformView → Custom Canvas-based waveform renderer

Features Implemented Audio Playback

Uses MediaPlayerManager

1.Loads sample.mp3 from assets

Supports:

· Play / Pause · Seek functionality · Playback progress tracking

Metadata Display
Uses MetadataUtils to extract:

· Track title · Artist name · Album artwork

If album art is unavailable, a default image is displayed.

Waveform Rendering
· Custom WaveformView (Canvas-based) · Displays a simple waveform visualization · Currently generates placeholder waveform data · Can be extended to render actual audio amplitude data

2.Musical Player – Equalizer Module

Overview

The EqualizerActivity provides advanced audio customization features for the Musical Player application.

It allows users to:

· Adjust 5-band equalizer levels · Apply predefined audio presets · Control Bass boost · Control Treble boost · Persist audio settings across sessions · Apply real-time effects to the MediaPlayer

Architecture

The Equalizer module follows a simplified MVVM pattern:

· EqualizerActivity → UI layer · EqualizerViewModel → State management · MediaPlayerManager → Audio processing engine · EqualizerStorage → Persistent storage (SharedPreferences)

Features Implemented Band Equalizer

Five frequency bands are adjustable using SeekBar controls:

· Band 1 · Band 2 · Band 3 · Band 4 · Band 5

Each band: Range: -500 to +500 (internally mapped) Offset applied because SeekBar does not support negative values The following presets are available:

· Rock · Jazz · Flat · Classical · Pop · Vocal
