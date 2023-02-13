from ultralytics import YOLO

model = YOLO("yolov8n.pt")  # load a pretrained model (recommended for training)

def scan(filePath):
    results = model(filePath)
    print(results)

