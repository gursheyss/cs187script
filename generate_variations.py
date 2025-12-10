#!/usr/bin/env python3
"""
Image Variation Generator for Disease Detection Testing

Generates variations of test images with different:
- Lighting: Low, Bright
- Photo conditions: Distance, Cropped, Angled

Output structure in total_images/:
- Original images copied as-is
- Variations named: {original_name}_{variation}.jpg
"""

import os
import shutil
from pathlib import Path

try:
    from PIL import Image, ImageEnhance, ImageFilter
except ImportError:
    print("Pillow not installed. Installing...")
    import subprocess
    subprocess.check_call(['pip', 'install', 'Pillow'])
    from PIL import Image, ImageEnhance, ImageFilter

# Configuration
SOURCE_DIR = Path("images")
OUTPUT_DIR = Path("total_images")
DISEASES = ["eczema", "melanoma", "psoriasis", "fungal_infection"]

# Variation types
VARIATIONS = {
    # Lighting variations
    "low_light": {"type": "lighting", "brightness": 0.5, "contrast": 0.8},
    "bright": {"type": "lighting", "brightness": 1.5, "contrast": 1.1},

    # Photo condition variations
    "distance": {"type": "distance", "scale": 0.5},  # Zoom out (smaller subject)
    "cropped": {"type": "cropped", "crop_ratio": 0.6},  # Center crop
    "angled": {"type": "angled", "angle": 15},  # Rotate image
}


def apply_lighting(img: Image.Image, brightness: float, contrast: float) -> Image.Image:
    """Apply lighting adjustments to image."""
    # Adjust brightness
    enhancer = ImageEnhance.Brightness(img)
    img = enhancer.enhance(brightness)

    # Adjust contrast
    enhancer = ImageEnhance.Contrast(img)
    img = enhancer.enhance(contrast)

    return img


def apply_distance(img: Image.Image, scale: float) -> Image.Image:
    """Simulate distance by scaling down and padding."""
    width, height = img.size

    # Scale down the image
    new_width = int(width * scale)
    new_height = int(height * scale)
    scaled = img.resize((new_width, new_height), Image.Resampling.LANCZOS)

    # Create new image with original size and paste scaled image in center
    result = Image.new(img.mode, (width, height), (128, 128, 128))  # Gray background
    offset_x = (width - new_width) // 2
    offset_y = (height - new_height) // 2
    result.paste(scaled, (offset_x, offset_y))

    return result


def apply_crop(img: Image.Image, crop_ratio: float) -> Image.Image:
    """Apply center crop to simulate cropped photo."""
    width, height = img.size

    # Calculate crop box (center crop)
    new_width = int(width * crop_ratio)
    new_height = int(height * crop_ratio)

    left = (width - new_width) // 2
    top = (height - new_height) // 2
    right = left + new_width
    bottom = top + new_height

    # Crop and resize back to original dimensions
    cropped = img.crop((left, top, right, bottom))
    result = cropped.resize((width, height), Image.Resampling.LANCZOS)

    return result


def apply_angle(img: Image.Image, angle: float) -> Image.Image:
    """Apply rotation to simulate angled photo."""
    # Rotate with expand=False to keep original dimensions
    # Fill background with gray
    result = img.rotate(angle, resample=Image.Resampling.BICUBIC,
                        expand=False, fillcolor=(128, 128, 128))
    return result


def apply_variation(img: Image.Image, variation_config: dict) -> Image.Image:
    """Apply a variation to an image based on config."""
    var_type = variation_config["type"]

    if var_type == "lighting":
        return apply_lighting(img,
                            variation_config["brightness"],
                            variation_config["contrast"])
    elif var_type == "distance":
        return apply_distance(img, variation_config["scale"])
    elif var_type == "cropped":
        return apply_crop(img, variation_config["crop_ratio"])
    elif var_type == "angled":
        return apply_angle(img, variation_config["angle"])
    else:
        raise ValueError(f"Unknown variation type: {var_type}")


def process_images():
    """Process all images and generate variations."""
    # Create output directory
    OUTPUT_DIR.mkdir(exist_ok=True)

    total_original = 0
    total_variations = 0

    for disease in DISEASES:
        source_disease_dir = SOURCE_DIR / disease
        output_disease_dir = OUTPUT_DIR / disease

        if not source_disease_dir.exists():
            print(f"Warning: Source directory not found: {source_disease_dir}")
            continue

        # Create output disease directory
        output_disease_dir.mkdir(exist_ok=True)

        # Get all jpg images
        images = list(source_disease_dir.glob("*.jpg"))
        print(f"\nProcessing {disease}: {len(images)} images")

        for img_path in images:
            img_name = img_path.stem  # e.g., "1" from "1.jpg"

            # Copy original image
            original_output = output_disease_dir / img_path.name
            shutil.copy2(img_path, original_output)
            total_original += 1
            print(f"  Copied original: {img_path.name}")

            # Load image for variations
            try:
                img = Image.open(img_path)
                # Convert to RGB if necessary (handles RGBA, grayscale, etc.)
                if img.mode != 'RGB':
                    img = img.convert('RGB')

                # Generate each variation
                for var_name, var_config in VARIATIONS.items():
                    try:
                        varied_img = apply_variation(img, var_config)

                        # Save variation
                        var_filename = f"{img_name}_{var_name}.jpg"
                        var_output = output_disease_dir / var_filename
                        varied_img.save(var_output, "JPEG", quality=90)
                        total_variations += 1
                        print(f"    Generated: {var_filename}")

                    except Exception as e:
                        print(f"    Error generating {var_name} for {img_path.name}: {e}")

                img.close()

            except Exception as e:
                print(f"  Error processing {img_path.name}: {e}")

    print(f"\n{'='*50}")
    print(f"Generation complete!")
    print(f"  Original images: {total_original}")
    print(f"  Variations generated: {total_variations}")
    print(f"  Total images: {total_original + total_variations}")
    print(f"  Output directory: {OUTPUT_DIR.absolute()}")


if __name__ == "__main__":
    print("Image Variation Generator")
    print("="*50)
    print(f"Source: {SOURCE_DIR}")
    print(f"Output: {OUTPUT_DIR}")
    print(f"Diseases: {', '.join(DISEASES)}")
    print(f"Variations: {', '.join(VARIATIONS.keys())}")
    print("="*50)

    process_images()
