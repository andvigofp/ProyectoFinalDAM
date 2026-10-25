from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

base = Path(__file__).resolve().parent
source = base / "10_perfil_original.png"
output = base / "10_perfil.png"
image = Image.open(source).convert("RGB")
draw = ImageDraw.Draw(image)
background = image.getpixel((10, 530))

# Sustituye la zona del botón original por dos acciones independientes.
draw.rectangle((10, 520, 379, 680), fill=background)
font = ImageFont.truetype(r"C:\Windows\Fonts\arialbd.ttf", 15)

buttons = [
    ((22, 530, 367, 581), (137, 152, 173), "Editar perfil"),
    ((22, 606, 367, 657), (176, 90, 90), "Cerrar sesión"),
]

for bounds, color, text in buttons:
    draw.rounded_rectangle(bounds, radius=26, fill=color)
    text_box = draw.textbbox((0, 0), text, font=font)
    text_width = text_box[2] - text_box[0]
    text_height = text_box[3] - text_box[1]
    center_x = (bounds[0] + bounds[2]) / 2
    center_y = (bounds[1] + bounds[3]) / 2
    draw.text(
        (center_x - text_width / 2, center_y - text_height / 2 - text_box[1]),
        text,
        font=font,
        fill=(255, 255, 255),
    )

image.save(output, optimize=True)
print(f"{output} ({output.stat().st_size} bytes)")
