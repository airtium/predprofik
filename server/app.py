from flask import Flask, request
from flask import jsonify
import psycopg2
import os
from dotenv import load_dotenv
from mlmodel import scan
load_dotenv(".env")
DB_SERVER = os.getenv('DB_SERVER')
DB_USER = os.getenv('DB_USER')
DB_PASSWORD = os.getenv('DB_PASSWORD')
DB_DATABASE = os.getenv('DB_DATABASE')
DB_PORT = os.getenv('DB_PORT')
TMP_DIR = os.getenv('TMP_DIR')
if not TMP_DIR:
    TMP_DIR = 'tmp'

app = Flask(__name__)

#ответ сервера если пользователь ничего не передал
@app.route("/", methods=["GET"])
def hello():
    return "Hello World!"


#добавление продуктов
@app.route('/product/add', methods=['GET', 'POST'])
def productAdd():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)

    except:
        print("Не могу установить соединение с базой данных")
        return 500
    if request.method == 'POST':
        #request_data = request.get_json()
        id_productname = request.form['id_productname']
        quantity = request.form['quantity']
        id_list = request.form['id_list']

    elif request.method == 'GET':
        id_productname = request.args.get('id_productname')
        quantity = request.args.get('quantity')
        id_list = request.args.get('id_list')
    else:
        print("Некорректный запрос")
        return 500
    if (not id_productname):
        print("Не указано название продукта")
        return 500
    if not quantity:
        quantity = 1
    if not id_list:
        id_list = 1

    cursor = conn.cursor()
    query = "INSERT INTO products (id_list, id_status, id_productname, quantity) VALUES ("+str(id_list)+", 1, "+str(id_productname)+", "+str(quantity)+") RETURNING id_product;"
    cursor.execute(query)
    id_product = cursor.fetchone()[0]
    conn.commit()
    conn.close()
    print('Добавлен продукт, его номер: ',id_product)
    return {'id_product':id_product, 'operation':'add'}


#изменение продуктов
@app.route('/product/update', methods=['GET', 'POST'])
def productUpdate():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)

    except:
        print("Не могу установить соединение с базой данных")
        return 500
    if request.method == 'POST':
        request_data = request.get_json()
        id_product = request_data['id_product']
        quantity = request_data['quantity']
        id_productname = request_data['id_productname']

    elif request.method == 'GET':
        id_product = request.args.get('id_product')
        quantity = request.args.get('quantity')
        id_productname = request.args.get('id_productname')
    else:
        print("Некорректный запрос")
        return 500
    if not id_product:
        print("Не указан продукт")
        return 500

    if id_productname and quantity:
        query = "UPDATE products SET id_productname="+str(id_productname)+", quantity="+str(quantity)+" WHERE id_product="+str(id_product)+""
    elif not id_productname:
        query = "UPDATE products SET quantity="+str(quantity)+" WHERE id_product="+str(id_product)+""
    elif not quantity:
        query = "UPDATE products SET id_productname="+str(id_productname)+" WHERE id_product="+str(id_product)+""

    cursor = conn.cursor()
    cursor.execute(query)
    conn.commit()
    conn.close()
    print('Продукт изменен, его номер: ',id_product)
    return {'id_product':id_product, 'operation':'update'}


#удаление продукта
@app.route('/product/delete', methods=['GET', 'POST'])
def productDelete():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)
    except:
        print("Не могу установить соединение с базой данных")
        return 500

    if request.method == 'POST':
        request_data = request.get_json()
        id_product = request_data['id_product']

    elif request.method == 'GET':
        id_product = request.args.get('id_product')
    else:
        print("Некорректный запрос")
        return 500
    if not id_product:
        print("Не указан продукт")
        return 500

    cursor = conn.cursor()
    query = "UPDATE products SET id_status=2 WHERE id_product="+str(id_product)+""
    cursor.execute(query)
    conn.commit()
    conn.close()
    print('Продукт удален, его номер: ',id_product)
    return {'id_product':id_product, 'operation':'delete'}

#вывод чек-листа со всеми продуктами всех пользователей
@app.route('/product/listall', methods=['GET', 'POST'])
def productsList():
    # здесь мы обращаемся к базе данных и показываем список продуктов
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)

    except:
        print("Не могу установить соединение с базой данных")
        return 500
    cursor = conn.cursor()
    query = ("SELECT products.id_product, product_names.name FROM products, product_names WHERE products.id_productname=product_names.id_productname AND products.id_status=1")
    cursor.execute(query)
    res = cursor.fetchall()
    products = []
    for (productID, productName) in res:
        print("Продукт номер "+str(productID)+", название: "+productName)
        products.append({'id_product':productID, 'name':productName})
    conn.close()
    return jsonify(products)


#показ чек-листов конкретного пользователя
@app.route('/user/list', methods=['GET', 'POST'])
def productsListuser():
    # здесь мы обращаемся к базе данных и показываем список продуктов
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)

    except:
        print("Не могу установить соединение с базой данных")
        return 500

    if request.method == 'POST':
        request_data = request.get_json()
        id_user = request_data['id_user']

    elif request.method == 'GET':
        id_user = request.args.get('id_user')
    else:
        print("Некорректный запрос")
        return 500
    if not id_user:
        print("Не указан пользователь")
        return 500

    cursor = conn.cursor()
    query = ("SELECT users.id_user, users.name, users.nickname, lists.id_list, products.id_product, product_names.name, products.quantity FROM products, lists, users, product_names WHERE products.id_productname=product_names.id_productname AND products.id_status=1 AND products.id_list=lists.id_list AND lists.id_user=users.id_user AND users.id_user="+str(id_user))
    cursor.execute(query)
    res = cursor.fetchall()
    products = []
    for (userID, userName, userNickname, listID, productID, productName, productQuantity) in res:
        print("Имя: "+userName+", ник: "+userNickname+", номер чек-листа: ", listID, ", номер продукта: ", productID, ", название: ", productName, ", количество: "+str(productQuantity))
        products.append({'id_user':userID, 'userName':userName , 'userNickname:':userNickname, 'id_list':listID, 'id_product':productID, 'name':productName, 'quantity':productQuantity})
    conn.close()
    return jsonify(products)


#добавление пользователя
@app.route('/user/add', methods=['GET', 'POST'])
def userAdd():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)

    except:
        print("Не могу установить соединение с базой данных")
        return 500
    if request.method == 'POST':
        request_data = request.get_json()
        email = request_data('email')
        name = request_data('name')
        nickname = request_data('nickname')
        password = request_data('password')

    elif request.method == 'GET':
        email = request.args.get('email')
        name = request.args.get('name')
        nickname = request.args.get('nickname')
        password = request.args.get('password')
    else:
        print("Некорректный запрос")
        return 500
    if not email:
        print("Не указан email")
        return 500
    if not name:
        print("Не указано имя")
        return 500
    if not nickname:
        print("Не указан ник")
        return 500
    if not password:
        print("Не указан пароль")
        return 500

    cursor = conn.cursor()
    query = "INSERT INTO users (email, name, nickname, password) VALUES ('"+email+"', '"+name+"' , '"+nickname+"', '"+password+"') RETURNING id_user;"
    cursor.execute(query)
    id_user = cursor.fetchone()[0]
    conn.commit()
    conn.close()
    print('Добавлен пользователь, имя: ',name,', ник: ',nickname,', его номер: ',id_user)
    return {'id_user':id_user, 'operation':'add'}


#изменение пользователя
@app.route('/user/update', methods=['GET', 'POST'])
def userUpdate():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)
    except:
        print("Не могу установить соединение с базой данных")
        return 500
    if request.method == 'POST':
        request_data = request.get_json()
        id_user = request_data('id_user')
        email = request_data('email')
        name = request_data('name')
        nickname = request_data('nickname')
        password = request_data('password')

    elif request.method == 'GET':
        id_user = request.ards.get('id_user')
        email = request.args.get('email')
        name = request.args.get('name')
        nickname = request.args.get('nickname')
        password = request.args.get('password')
    else:
        print("Некорректный запрос")
        return 500
    if not id_user:
        print("Не указан идентификатор пользователя")
        return 500

    cursor = conn.cursor()
    query = "UPDATE users SET email='"+email+"', name='"+name+"', nickname='"+nickname+"', password='"+password+"' WHERE id_user="+str(id_user)+""
    cursor.execute(query)
    conn.commit()
    conn.close()
    return {'id_user':id_user, 'operation':'update'}


#добавление чек-листа
@app.route('/list/add', methods=['GET', 'POST'])
def listAdd():
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)
    except:
        print("Не могу установить соединение с базой данных")
        return 500
    if request.method == 'POST':
        request_data = request.get_json()
        id_user = request.args.get('id_user')

    elif request.method == 'GET':
        id_user = request.ards.get('id_user')

    # получение последнего номера чек-листа пользователя чтобы увеличить его на 1
    cursor = conn.cursor()
    query = "SELECT number FROM lists WHERE id_user="+str(id_user)+" ORDER BY number DESC LIMIT 1"
    cursor.execute(query)
    if cursor.rowcount>0:
        number = cursor.fetchone()[0]+1
    else:
        number = 1
    query = "INSERT INTO lists (id_user, number) VALUES ("+str(id_user)+", "+str(number)+") RETURNING id_list;"
    cursor.execute(query)
    id_list = cursor.fetchone()[0]
    conn.commit()
    conn.close()
    print('Добавлен чек-лист, его ид: ',id_list, ', номер чек-листа пользователя: ', number)
    return {'id_list':id_list, 'number':number}


#вывод пользователей
@app.route('/users/listall', methods=['GET', 'POST'])
def usersList():
    # здесь мы обращаемся к базе данных и показываем список пользователей
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)
    except:
        print("Не могу установить соединение с базой данных")
        return 500
    cursor = conn.cursor()
    query = ("SELECT id_user,name,nickname,email FROM users")
    cursor.execute(query)
    res = cursor.fetchall()
    users = []
    for (userID, userName, userNickname, userEmail) in res:
        users.append({'id_user':userID, 'name':userName, 'nickname':userNickname, 'email':userEmail})
        print("Пользователь номер ", userID, ' имя: ', userName, ' ник: ', userNickname, ", почта: ",userEmail)
    conn.close()
    return jsonify(users)


#показ справочник названий продуктов
@app.route('/product/namesall', methods=['GET', 'POST'])
def productnameList():
    # здесь мы обращаемся к базе данных и показываем справочник продуктов
    try:
        conn = psycopg2.connect(database=DB_DATABASE,
                                host=DB_SERVER,
                                user=DB_USER,
                                password=DB_PASSWORD,
                                port=DB_PORT)
    except:
        print("Не могу установить соединение с базой данных")
        return 500
    cursor = conn.cursor()
    query = ("SELECT id_productname,name FROM product_names")
    cursor.execute(query)
    res = cursor.fetchall()
    names = []
    for (productID, productName) in res:
        names.append({'id_productname':productID, 'name':productName})

        print("Название продукта ", productID, ': ', productName)
    conn.close()
    return jsonify(names)


#Форма для проверки модели машинного обучения (загрузка файла с картинкой и отправка запроса)
@app.route('/product/scanform', methods=['GET', 'POST'])
def productScanForm():
    form="""
    <!doctype html>
    <title>Загрузить новый файл</title>
    <h1>Загрузить новый файл</h1>
    <form method=post enctype=multipart/form-data action='/product/scan'>
      <input type=file name=file>
      <input type=submit value=Upload>
    </form>
    </html>    
    
    """
    return form


#Распознавание продукта с помощью модели машинного обучения, обработку делаем в отдельном файле
# здесь получаем файл, сохраняем его на диск во временную папку и передаем на обработку
@app.route('/product/scan', methods=['GET', 'POST'])
def productScan():
    if request.method == 'POST':
        request_data = request.get_json()
        id_product = request_data['id_product']

    elif request.method == 'GET':
        id_product = request.args.get('id_product')
    else:
        print("Некорректный запрос")
        return 500
    if not id_product:
        print("Не указан продукт, распознавание изображения без изменения продукта")

    request_files = request.files
    if not request_files:
        print('Нет файлов в запросе')
        return 500
    request_file = request.files[0]
    if not request_file:
        print('Не могу выбрать файл')
        return 500
    if request_file.filename == '':
        print('Нет выбранного файла')
        return 500
    # создаем временную директорию для файлов
    if not os.path.exists(TMP_DIR+'/upload'):
        os.makedirs(TMP_DIR+'/upload')
    filePath = TMP_DIR+"/img.jpg"
    # сохраняем файл
    request_file.save(filePath)

    productMLName = scan(filePath)

    #удаляем файл, чтобы не засорять сервер
    os.remove(filePath)

    return productMLName



if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000)