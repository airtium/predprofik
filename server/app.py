from flask import Flask, request
from flask import jsonify
import psycopg2
import os
from dotenv import load_dotenv
load_dotenv(".env")
DB_SERVER = os.getenv('DB_SERVER')
DB_USER = os.getenv('DB_USER')
DB_PASSWORD = os.getenv('DB_PASSWORD')
DB_DATABASE = os.getenv('DB_DATABASE')
DB_PORT = os.getenv('DB_PORT')
app = Flask(__name__)

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
        print("Something is wrong")
        return 500
    id_productname = request.args.get('id_productname')
    quantity = request.args.get('quantity')
    id_list = request.args.get('id_list')
    cursor = conn.cursor()
    query = "INSERT INTO products (id_list, id_status, id_productname, quantity) VALUES ("+id_list+", 1, "+id_productname+", "+quantity+")"
    cursor.execute(query)
    conn.commit()
    conn.close()
    return "Product is added!"
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
        print("Something is wrong")
        return 500
    product = request.args.get('product')
    quantity = request.args.get('quantity')
    id_status = request.args.get('id_status')
    id_product = request.args.get('id_product')
    cursor = conn.cursor()
    query = "UPDATE products SET id_status="+id_status+", name='"+product+"', quantity="+quantity+" WHERE id_product="+id_product+""
    cursor.execute(query)
    conn.commit()
    conn.close()
    return " "+product+" is edited!"

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
        print("Something is wrong")
        return 500

    id_product = request.args.get('id_product')
    cursor = conn.cursor()
    query = "UPDATE products SET id_status=2 WHERE id_product="+id_product+""
    cursor.execute(query)
    conn.commit()
    conn.close()
    return " "+id_product+" is deleted!"


#вывод чек-листа
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
        print("Something is wrong")
        return 500
    cursor = conn.cursor()
    query = ("SELECT products.id_product, product_names.name FROM products, product_names WHERE products.id_productname=product_names.id_productname AND products.id_status=1")
    cursor.execute(query)
    res = cursor.fetchall()
    a = []
    for (productID, productName) in res:
        a.append("Продукт номер "+str(productID)+", название: "+productName)
        print("Получили из базы продукт номер ", productID)
    conn.close()
    b=''
    for x in a:
        b=b+"<p>"+x+"</p>"
    return b
#показ чек-листа конкретного пользователя
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
        print("Something is wrong")
        return 500
    id_user = request.args.get('id_user')
    cursor = conn.cursor()
    query = ("SELECT users.name, users.nickname, product_names.name, products.quantity FROM products, lists, users, product_names WHERE products.id_productname=product_names.id_productname AND products.id_status=1 AND products.id_list=lists.id_list AND lists.id_user=users.id_user AND users.id_user="+id_user)
    cursor.execute(query)
    res = cursor.fetchall()
    a = []
    for (userName, userNickname, productName, productQuantity) in res:
        a.append("<tr><td>"+userName+"</td><td>"+userNickname+"</td><td>"+productName+"</td><td>"+str(productQuantity)+"</td></tr>")
    conn.close()
    b="<table><tr><td>Имя</td><td>Никнейм</td><td>Продукт</td><td>Количество</td></tr>"
    for x in a:
        b=b+x
    b=b+'</table>'
    return b
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
        print("Something is wrong")
        return 500
    email = request.args.get('email')
    name = request.args.get('name')
    nickname = request.args.get('nickname')
    password = request.args.get('password')
    cursor = conn.cursor()
    query = "INSERT INTO users (email, name, nickname, password) VALUES ('"+email+"', '"+name+"' , '"+nickname+"', '"+password+"')"
    cursor.execute(query)
    conn.commit()
    conn.close()
    return "User "+name+" is added!"
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
        print("Something is wrong")
        return 500
    email = request.args.get('email')
    name = request.args.get('name')
    nickname = request.args.get('nickname')
    password = request.args.get('password')
    id_user = request.ards.get('id_user')
    user = request.ards.get('user')
    cursor = conn.cursor()
    query = "UPDATE users SET email='"+email+"', name='"+name+"', nickname='"+nickname+"', password='"+password+"' WHERE id_user="+id_user+""
    cursor.execute(query)
    conn.commit()
    conn.close()
    return " "+user+" is edited!"
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
        print("Something is wrong")
        return 500
    id_user = request.args.get('id_user')
    number = request.args.get('number')
    cursor = conn.cursor()
    query = "INSERT INTO lists (id_user, number) VALUES ("+id_user+", "+number+")"
    cursor.execute(query)
    conn.commit()
    conn.close()
    return "List is added!"
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
        print("Something is wrong")
        return 500
    cursor = conn.cursor()
    query = ("SELECT id_user,email FROM users")
    cursor.execute(query)
    res = cursor.fetchall()
    a = []
    for (userID, userEmail) in res:
        a.append("Пользователь номер "+str(userID)+", почта: "+userEmail)
        print("Получили из базы пользователя номер ", userID)
    conn.close()
    b=''
    for x in a:
        b=b+"<p>"+x+"</p>"
    return b
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
        print("Something is wrong")
        return 500
    cursor = conn.cursor()
    query = ("SELECT * FROM product_names")
    cursor.execute(query)
    res = cursor.fetchall()
    names = []
    for (productID, productName) in res:
        names.append({productID:productName})

        print("Получили из базы номер ", productID)
    conn.close()
    return jsonify(names)



if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000)