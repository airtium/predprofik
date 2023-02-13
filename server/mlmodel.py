from ultralytics import YOLO

model = YOLO("runs/classify/train2/weights/best.pt")  # load a pretrained model (recommended for training)

def scan(filePath):
    results = model(filePath)
    print(results)

