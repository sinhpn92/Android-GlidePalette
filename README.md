Android GlidePalette
=======
[![](https://jitpack.io/v/sinhpn92/Android-GlidePalette.svg)](https://jitpack.io/#sinhpn92/Android-GlidePalette)

Use palette for background view and text by glide v4 image loader. It's rewrite by kotlin.

![Alt sample](https://github.com/sinhpn92/Android-GlidePalette/raw/master/static/Screenshot_1577178027.png)



# Sample

```java
Glide.with(this).load(url)
         .listener(GlidePalette.with(url)
               .use(GlidePalette.Profile.MUTED_DARK)
                   .intoBackground(textView)
                   .intoTextColor(textView)

               .use(GlidePalette.Profile.VIBRANT)
                    .intoBackground(titleView, GlidePalette.Swatch.RGB)
                    .intoTextColor(titleView, GlidePalette.Swatch.BODY_TEXT_COLOR)
                    .crossfade(true)
         );
         .into(imageView);
```

## Installation

Add it in your root build.gradle at the end of repositories:

```
allprojects {
         repositories {
                  ...
                  maven { url 'https://jitpack.io' }
         }
}
```

Add the dependency:
```
dependencies {
        implementation 'com.github.sinhpn92:Android-GlidePalette:Tag'
}
```

First, init GlidePalette with an **Url**

```java
GlidePalette.with(url)
```

## Palettes

You can successively use following Palettes :

- Palette.VIBRANT
- Palette.VIBRANT_DARK
- Palette.VIBRANT_LIGHT
- Palette.MUTED
- Palette.MUTED_DARK
- Palette.MUTED_LIGHT

```java
.use(GlidePalette.Profile.MUTED_DARK)
```

**Each time you call "use" the next modification will follow this Profile**

```java
.use(GlidePalette.Profile.MUTED_DARK)
    //next operations will use Profile.MUTED_DARK
.use(GlidePalette.Profile.VIBRANT)
    //next operations will use Profile.VIBRANT
```

## Swatches

With the following Swatches

- RGB
- TITLE_TEXT_COLOR
- BODY_TEXT_COLOR

## Targets

Into Backgrounds

```java
.intoBackground(view)
.intoBackground(view,Swatch.RGB)
```

And TextView Color

```java
.intoTextColor(textView)
.intoTextColor(textView,Swatch.TITLE_TEXT_COLOR)
```

with optional Background Crossfade effect
```java
.crossfade(true)
    // will use default 300ms crossfade
.crossfade(true, 1000)
    // specify own crossfade speed in ms
```

# CallBack

Or simply return into CallBack

```java
.intoCallBack(
    object : BitmapPalette.CallBack {
         override fun onPaletteLoaded(@Nullable palette: Palette?) { //specific task

         }
})
```
