export class user{
    datosLogin(email:string, pass:string) {
        this.email = email;
        this.pwd = pass;
        this.pwd2 = pass;
    }
    nombre:string
    email:string
    pwd:string
    pwd2:string

    constructor(){
        this.nombre="",
        this.email="",
        this.pwd="",
        this.pwd2=""
    }

    datosRegistro(nombre:string, email:string, pwd1:string, pwd2:string){
        this.nombre=nombre;
        this.email=email;
        this.pwd=pwd1;
        this.pwd2=pwd2;
    }
}