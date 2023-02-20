from ultralytics import YOLO #основная программма задающая нейронную сеть

model = YOLO("runs/classify/train8/weights/best.pt") #обученнаяя модель на тренировочном дата сете

def scan(filePath):
    results = model(filePath)[0] # результаты работы нейронной сети с данным ей объектом 
    print("Нейронка вернула результат:")
    print(results)
    results = results.probs.tolist() 
    print("Максимальное значение вероятности: ",max(results))
    print("Элемент с максимальным значением: ",results.index(max(results))+1)
    return results.index(max(results))+1