# DMX Tesseract

A plugin for the [DMX platform](https://github.com/dmx-systems/dmx-platform).
It makes the [Tesseract](https://github.com/tesseract-ocr/tesseract) OCR engine available on the DMX platform.

## Requirements

The DMX Tesseract plugin relies on a local native Tesseract [installation](https://tesseract-ocr.github.io/tessdoc/Installation.html).

## Installation

Released versions are available at:  
https://download.dmx.systems/plugins/dmx-tesseract/

Snapshot versions (work in progress) are available at:  
https://download.dmx.systems/ci/dmx-tesseract/

As with any DMX plugin you install it by copying the respective `.jar` file to DMX's `bundle-deploy/` directory.

## Configuration

| Property                | Required | Description                                               |
| --------                | -------- | -----------                                               |
| dmx.tesseract.data_path | yes      | Path to the `tessdata` directory of your local Tesseract installation, e.g. `/usr/share/tesseract-ocr/tessdata` |

After configuration a restart of the DMX platform is required.

## How it works

There is neither a GUI nor a HTTP interface. Instead the DMX Tesseract plugin provides an OSGi service (`systems.dmx.tesseract.TesseractService`) to be consumable by other DMX plugins.

The service consists of only one method:
```
String doOCR(String repoPath)
```
The image or PDF file to be OCRed is expected to exist in the DMX file repository. The result is the recognized text.

For accessing the native Tesseract (C++) API from Java, [Tess4j](https://github.com/nguyenq/tess4j) is utilized, a JNA based wrapper.

The DMX Tesseract service is consumed e.g. by the [dmx-pdf-search](https://github.com/dmx-systems/dmx-pdf-search) plugin.

## Version History

**1.0** -- Dec 18, 2023

* Provides a service with a `doOCR()` method.
* Compatible with DMX 5.3.4 or later
