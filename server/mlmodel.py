from ultralytics import YOLO

model = YOLO("runs/classify/train8/weights/best.pt")

def scan(filePath):
    results = model(filePath)[0]
    print("Нейронка вернула результат:")
    print(results)
    results = results.probs.tolist()
    print("Максимальное значение вероятности: ",max(results))
    print("Элемент с максимальным значением: ",results.index(max(results))+1)
    return results.index(max(results))+1
