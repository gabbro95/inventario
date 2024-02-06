from kivy.lang import Builder
from kivy.properties import DictProperty
from kivy.properties import StringProperty
from kivy.factory import Factory
from kivy.animation import Animation

from kivymd.app import MDApp
from kivy.uix.screenmanager import ScreenManager, Screen
from kivymd.uix.button import MDFlatButton
from kivymd.uix.dialog import MDDialog
from kivymd.uix.bottomsheet import MDCustomBottomSheet
from kivymd.uix.card import MDCardSwipe
from kivymd.uix.screen import MDScreen
from kivymd.uix.chip import MDChip

from kivymd.uix.list import IRightBodyTouch, ThreeLineAvatarIconListItem
from kivymd.uix.selectioncontrol import MDCheckbox

import pyrebase

config = {
    "apiKey": "AIzaSyBnURDGM5HPddrPiXooC1QFvjeUhxAqy6M",
    "authDomain": "inventario-9d193.firebaseapp.com",
    "databaseURL": "https://inventario-9d193-default-rtdb.firebaseio.com",
    "projectId": "inventario-9d193",
    "storageBucket": "inventario-9d193.appspot.com",
    "messagingSenderId": "823581297262",
    "appId": "1:823581297262:web:c72bbaf6f95160a707f47f",
    "measurementId": "G-C7J66H13MN"
    }

firebase = pyrebase.initialize_app(config)
database = firebase.database()
auth = firebase.auth()
email = 'gabbro@gmail.com'
password = 'Pi@1997'

help_str = '''
ScreenManager:
    WelcomeScreen:
    MainScreen:
    LoginScreen:
    SignupScreen:
    CheckElementScreen:
    CreateElementScreen:
    WishListScreen:
    UpdateInventarioScreen:

<WelcomeScreen>:
    name:'welcomescreen'
    MDLabel:
        text:'Login'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.9}
    MDLabel:
        text:'&'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.7}
    MDLabel:
        text:'Signup'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.5}
    MDRaisedButton:
        text:'Login'
        pos_hint : {'center_x':0.4,'center_y':0.3}
        size_hint: (0.13,0.1)
        on_press: 
            app.verify_credential()
            root.manager.current = 'loginscreen'
            root.manager.transition.direction = 'left'
    MDRaisedButton:
        text:'Signup'
        pos_hint : {'center_x':0.6,'center_y':0.3}
        size_hint: (0.13,0.1)
        on_press:
            root.manager.current = 'signupscreen'
            root.manager.transition.direction = 'left'
        
<LoginScreen>:
    name:'loginscreen'
    MDLabel:
        text:'Login'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.9}
    MDTextField:
        id:signup_email
        pos_hint: {'center_y':0.65,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Email'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right: 'email'
        icon_right_color: app.theme_cls.primary_color
        required: True
        mode: "rectangle"
    MDTextField:
        id:login_password
        pos_hint: {'center_y':0.45,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Password'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right: 'account-key'
        icon_right_color: app.theme_cls.primary_color
        required: True
        mode: "rectangle"
    MDRaisedButton:
        text:'Login'
        size_hint: (0.13,0.07)
        pos_hint: {'center_x':0.5,'center_y':0.2}
        on_press:
            app.login()
            app.username_changer() 
    MDTextButton:
        text: 'Create an account'
        pos_hint: {'center_x':0.5,'center_y':0.1}
        on_press:
            root.manager.current = 'signupscreen'
            root.manager.transition.direction = 'up'

<SignupScreen>:
    name:'signupscreen'
    MDLabel:
        text:'Signup'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.9}
    MDTextField:
        id:signup_username
        pos_hint: {'center_y':0.7,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Username'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right: 'alphabetical'
        icon_right_color: app.theme_cls.primary_color
        required: True
    MDTextField:
        id:signup_email
        pos_hint: {'center_y':0.55,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Email'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right: 'alphabetical'
        icon_right_color: app.theme_cls.primary_color
        required: True
    MDTextField:
        id:signup_password
        pos_hint: {'center_y':0.4,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Password'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right: 'account-key'
        icon_right_color: app.theme_cls.primary_color
        required: True
    MDRaisedButton:
        text:'Signup'
        size_hint: (0.13,0.07)
        pos_hint: {'center_x':0.5,'center_y':0.2}
        on_press: app.signup()
    MDTextButton:
        text: 'Already have an account'
        pos_hint: {'center_x':0.5,'center_y':0.1}
        on_press:
            root.manager.current = 'loginscreen'
            root.manager.transition.direction = 'down'

<MainScreen>:
    name: 'mainscreen'
    MDLabel:
        id:username_info
        text:'Benvenuto'
        font_style:'H4'
        halign:'center'
    MDScreen:
        MDFloatingActionButtonSpeedDial:
            id: speed_dial
            data: app.data
            root_button_anim: True
            adaptive_weight: True

<ItemForCustomBottomSheet@OneLineIconListItem>
    on_press: app.custom_sheet.dismiss()
    icon: ""

    IconLeftWidget:
        icon: root.icon    

<ContentCustomSheet@BoxLayout>:
    orientation: "vertical"
    size_hint_y: None
    height: "150dp"
    MDGridLayout:
        cols: 1
        adaptive_height: True
        ItemForCustomBottomSheet:
            icon: "fridge"
            text: "Frigo"
            on_press: app.frigo()
        ItemForCustomBottomSheet:
            icon: "snowflake"
            text: "Congelatore"
            on_press: app.congela()
        ItemForCustomBottomSheet:
            icon: "dresser"
            text: "Credenza"
            on_press: app.credenza()

<SwipeToDeleteItem>:
    size_hint_y: None
    height: content.height
    MDCardSwipeLayerBox:
        padding: "8dp"
        MDIconButton:
            icon: "trash-can"
            pos_hint: {"center_y": .5}
            on_release: app.remove_item(root)
    MDCardSwipeFrontBox:
        MDIconButton:
            icon: "cog"
            size_hint: (0.15,0.07)
            pos_hint: {"center_y": .5}
            on_press: app.update_quantity(root)
        ThreeLineAvatarIconListItem:
            id: content
            text: root.text
            secondary_text: root.secondary_text
            tertiary_text: root.tertiary_text
            _no_ripple_effect: True
        IconLeftWidget:
            icon: "plus"
            size_hint: (0.11,0.07)
            on_release: app.plus_item(root)
        IconRightWidget:
            icon: "minus" 
            size_hint: (0.11,0.07)
            on_release: app.minus_item(root)

<CheckElementScreen>:
    name: 'checkelementscreen'
    MDScreen:
        MDBoxLayout:
            orientation: "vertical"
            MDBoxLayout:
                padding: 50
                MDScrollView:
                    MDList:
                        id: md_list
                        spacing: "8dp"
        MDFloatingActionButtonSpeedDial:
            id: speed_dial
            data: app.data
            root_button_anim: True
            hint_animation: True
            adaptive_weight: True

<CreateElementScreen>:            
    name:'createelementscreen'
    MDLabel:
        text:'Crea elemento!'
        font_style:'H2'
        halign:'center'
        pos_hint: {'center_y':0.85}
    MDTextField:
        id:market
        pos_hint: {'center_y':0.7,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Supermercato'
        icon_right: 'alphabetical'
        icon_right_color: app.theme_cls.primary_color
    MDTextField:
        id:quantity
        pos_hint: {'center_y':0.6,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Quantità'
        icon_right: 'numeric'
        icon_right_color: app.theme_cls.primary_color
    MDTextField:
        id:name
        pos_hint: {'center_y':0.5,'center_x':0.5}
        size_hint : (0.7,0.1)
        hint_text: 'Nome'
        icon_right: 'food'
        helper_text:'Required'
        helper_text_mode:  'on_error'
        icon_right_color: app.theme_cls.primary_color
        required: True
    MDBoxLayout:
        orientation: "vertical"
        adaptive_size: True
        spacing: "12dp"
        padding: "56dp"
        pos_hint: {"center_x": .5, "center_y": .3}
        MDLabel:
            text: "Seleziona la posizione!"
            bold: True
            font_style: "H5"
            halign:'center'
        MDBoxLayout:
            id: chip_box
            adaptive_size: True
            spacing: "8dp"
            padding:  20
            MyChip:
                id: chip_congelatore
                text: "Congelatore"
                on_active: if self.active: root.removes_marks_all_chips(self) 
            MyChip:
                id: chip_frigo
                text: "Frigo"
                on_active: if self.active: root.removes_marks_all_chips(self)
            MyChip:
                id: chip_credenza
                text: "Credenza"
                on_active: if self.active: root.removes_marks_all_chips(self)
    MDRaisedButton:
        text:'Crea'
        size_hint: (0.13,0.07)
        pos_hint: {'center_y':0.1, 'center_x':0.5}
        on_press: app.crete_inventario_screen()
    MDFloatingActionButtonSpeedDial:
        id: speed_dial
        data: app.data
        root_button_anim: True
        hint_animation: True
        adaptive_weight: True
        
<ListItemWithCheckbox>:
    IconLeftWidget:
        RightCheckbox:
            on_release: app.get_wish_element(root)
    
<WishListScreen>:
    name: 'wishlistscreen'
    MDTextField:
        id:name
        pos_hint: {'center_y':0.9, 'center_x':0.75}
        size_hint : (0.4,0.1)
        hint_text: 'Nome'
        icon_right: 'food'
        icon_right_color: app.theme_cls.primary_color
    MDTextField:
        id:market
        pos_hint: {'center_y':0.75, 'center_x':0.75}
        size_hint : (0.4,0.1)
        hint_text: 'Supermercato'
        icon_right: 'food'
        icon_right_color: app.theme_cls.primary_color
    MDTextField:
        id:quantity
        pos_hint: {'center_y':0.6, 'center_x':0.75}
        size_hint : (0.4,0.1)
        hint_text: 'Quantità'
        icon_right: 'food'
        icon_right_color: app.theme_cls.primary_color
    MDBoxLayout:
        MDGridLayout:
            cols: 1
            MDBoxLayout:
                MDScrollView:
                    MDList:
                        id: scroll
        MDBoxLayout:
            adaptive_height: True
            orientation: "vertical"
            spacing: "3dp"
            padding: 80
            MDRaisedButton:
                text:'Crea'
                pos_hint: {'center_y':0.9, 'center_x':0.3}
                on_press: app.create_wish_element()
            MDRaisedButton:
                text:'Aggiorna'
                pos_hint: {'center_y':0.7, 'center_x':0.3}
                on_press: app.upgrade_wish_element()
    MDFloatingActionButtonSpeedDial:
        id: speed_dial
        data: app.data
        root_button_anim: True
        hint_animation: True
        adaptive_weight: True
<UpdateInventarioScreen>:
    name: 'updateinventarioscreen'
    MDTextField:
        id:quantity
        pos_hint: {'center_y':0.7, 'center_x':0.5}
        size_hint : (0.4,0.1)
        hint_text: 'Quantità'
        icon_right: 'food'
        icon_right_color: app.theme_cls.primary_color
    MDRaisedButton:
        text:'Crea'
        size_hint: (0.13,0.07)
        pos_hint: {'center_y':0.45, 'center_x':0.5}
        on_press: app.update()
    MDFloatingActionButtonSpeedDial:
        id: speed_dial
        data: app.data
        root_button_anim: True
        hint_animation: True
        adaptive_weight: True
'''

class MyChip(MDChip):
    icon_check_color = (0, 0, 0, 1)
    text_color = (0, 0, 0, 0.5)
    _no_ripple_effect = True

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.bind(active=self.set_chip_bg_color)
        self.bind(active=self.set_chip_text_color)

    def set_chip_bg_color(self, instance_chip, active_value: int):
        '''
        Will be called every time the chip is activated/deactivated.
        Sets the background color of the chip.
        '''

        self.md_bg_color = (
            (0, 0, 0, 0.4)
            if active_value
            else (
                self.theme_cls.bg_darkest
                if self.theme_cls.theme_style == "Light"
                else (
                    self.theme_cls.bg_light
                    if not self.disabled
                    else self.theme_cls.disabled_hint_text_color
                )
            )
        )

    def set_chip_text_color(self, instance_chip, active_value: int):
        Animation(
            color=(0, 0, 0, 1) if active_value else (0, 0, 0, 0.5), d=0.2
        ).start(self.ids.label)

class SwipeToDeleteItem(MDCardSwipe):
    '''Custom list item.'''
    text = StringProperty()
    secondary_text = StringProperty()
    tertiary_text = StringProperty()
class ListItemWithCheckbox(ThreeLineAvatarIconListItem):
    '''Custom list item.'''
    text = StringProperty()
    secondary_text = StringProperty()
    tertiary_text = StringProperty()
class RightCheckbox(IRightBodyTouch, MDCheckbox):
    '''Custom right container.'''
    
class WelcomeScreen(Screen):
    pass
class MainScreen(Screen):
    pass
class LoginScreen(Screen):
    pass
class SignupScreen(Screen):
    pass
class CheckElementScreen(Screen):
    pass
class CreateElementScreen(MDScreen):
    def removes_marks_all_chips(self, selected_instance_chip):
        for instance_chip in self.ids.chip_box.children:
            if instance_chip != selected_instance_chip:
                instance_chip.active = False
class WishListScreen(Screen):
    pass
class UpdateInventarioScreen(Screen):
    pass

sm = ScreenManager()
sm.add_widget(WelcomeScreen(name = 'loginscreen'))
sm.add_widget(MainScreen(name = 'mainscreen'))
sm.add_widget(LoginScreen(name = 'loginscreen'))
sm.add_widget(SignupScreen(name = 'signupscreen'))
sm.add_widget(CheckElementScreen(name = 'checkelementscreen'))
sm.add_widget(CreateElementScreen(name = 'createelementscreen'))
sm.add_widget(WishListScreen(name = 'wishlistscreen'))
sm.add_widget(UpdateInventarioScreen(name = 'updateinventarioscreen'))

class Inventario(MDApp):
    data = DictProperty()
    custom_sheet = None
    
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.theme_cls.theme_style = "Dark"
        self.theme_cls.primary_palette = "Orange"
        self.strng = Builder.load_string(help_str)
        self.username = None
        self.usr = None
        self.position = None
        self.name_update = None
        self.func_update = None
        self.select_wish_item = {}
        
    def build(self):
        self.url  = "https://inventario-9d193-default-rtdb.firebaseio.com/.json"
        self.data = {
            'Spesa': [
                'notebook-plus',
                "on_press", lambda x: self.wish_list(),
            ],
            'Crea': [
                'creation',
                "on_press", lambda x: self.view_crete_screen(),
            ],
            'Ricerca': [
                'database-search',
                "on_release", lambda x: self.show_custom_bottom_sheet()
            ],
        }
        return self.strng
    
    def view_crete_screen(self):
        self.strng.get_screen('createelementscreen').ids.name.text = ""
        self.strng.get_screen('createelementscreen').ids.market.text = ""
        self.strng.get_screen('createelementscreen').ids.quantity.text = ""
        self.strng.get_screen('createelementscreen').manager.current = 'createelementscreen'
    
    def crete_inventario_screen(self):
        name = self.strng.get_screen('createelementscreen').ids.name.text
        market = self.strng.get_screen('createelementscreen').ids.market.text
        quantity = self.strng.get_screen('createelementscreen').ids.quantity.text
        if self.strng.get_screen('createelementscreen').ids.chip_frigo.active:
            position = self.strng.get_screen('createelementscreen').ids.chip_frigo.text
        elif self.strng.get_screen('createelementscreen').ids.chip_congelatore.active:
            position = self.strng.get_screen('createelementscreen').ids.chip_frigo.text
        elif self.strng.get_screen('createelementscreen').ids.chip_credenza.active:
            position = self.strng.get_screen('createelementscreen').ids.chip_frigo.text
        else:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Mancata selezione',text = 'Non dimenticare di selezionare la posizione',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        if name.split() == []:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Mancato inserimento',text = 'Compilare i campi necessari',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        if len(market.split())>1 or len(name.split())>1:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Scrittura username errata',text = 'Inserire un nome senza spazi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        else:
            try:
                data = { 
                    position: {
                        name: {
                            "Supermercato": market, 
                            "Quantità": quantity
                        }
                    }
                }
                database.child('users').child(self.username).child('Inventario').update(data, self.usr['idToken'])
            except:
                cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
                self.dialog = MDDialog(title = 'Database errore',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
                self.dialog.open()
    
    def callback(self, position):
        try:
            self.strng.get_screen('checkelementscreen').ids.md_list.clear_widgets()
            user_info = database.child('users').child('gabbro').child('Inventario').child(position).get(self.usr['idToken'])
            for key in user_info.val():
                info = database.child('users').child('gabbro').child('Inventario').child(position).child(key).get(self.usr['idToken'])
                _text = {}
                c = 0
                _text[c] = key
                for key_ in info:
                    c = c + 1
                    _text[c] = key_.val()
                    if c == 2:
                        self.strng.get_screen('checkelementscreen').ids.md_list.add_widget(
                            SwipeToDeleteItem(text=_text[0], secondary_text=_text[2], tertiary_text=_text[1])
                        )
            self.strng.get_screen('checkelementscreen').manager.current = 'checkelementscreen'
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'Inventario vuoto',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
    
    def remove_item(self, instance):
        try:
            database.child('users').child(self.username).child('Inventario').child(instance.text).remove(self.usr['idToken'])
            self.strng.get_screen('checkelementscreen').ids.md_list.remove_widget(instance)
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
    
    def plus_item(self, instance):
        try:
            if self.name_update != None:
                self.func_update = "plus"
                self.strng.get_screen('updateinventarioscreen').manager.current = 'updateinventarioscreen'
            else:
                user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child(instance.text).get(self.usr['idToken'])
                c = 0
                for key in user_info:
                    if c == 0:
                        n = str(int(key.val()) + 1)
                        data = {
                            'Quantità': n
                        }
                        user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child('Latte').update(data, self.usr['idToken'])
                    c = c + 1
                self.callback(self.position)
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        
    def minus_item(self, instance):
        try:
            if self.name_update != None:
                self.func_update = "minus"
                self.strng.get_screen('updateinventarioscreen').manager.current = 'updateinventarioscreen'
            else:
                user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child(instance.text).get(self.usr['idToken'])
                c = 0
                for key in user_info:
                    if c == 0:
                        n = str(int(key.val()) - 1)
                        data = {
                            'Quantità': n
                        }
                        user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child(instance.text).update(data, self.usr['idToken'])
                    c = c + 1
                self.callback(self.position)
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()

    def update_quantity(self, instance):
        if self.name_update == None:
            self.name_update = instance.text
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Scelta',text = 'Ora è possibile selezionare la funzione desiderata "+" o "-"',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        else:
            self.name_update = None
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Scelta',text = 'La scelta è stata dimenticata',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
     
    def update(self):
        quantity = self.strng.get_screen('updateinventarioscreen').ids.quantity.text
        if quantity.split() == []:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Mancato inserimento',text = 'Inserire la quantità',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        else:
            try:
                user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child(self.name_update).get(self.usr['idToken'])
                c = 0
                for key in user_info:
                    if c == 0:
                        if self.func_update == "minus":
                            n = str(int(key.val()) - int(quantity))
                        elif self.func_update == "plus":
                            n = str(int(key.val()) + int(quantity))
                        data = {
                            'Quantità': n
                        }
                        user_info = database.child('users').child('gabbro').child('Inventario').child(self.position).child(self.name_update).update(data, self.usr['idToken'])
                    c = c + 1
                self.name_update = None
                self.func_update = None
                self.callback(self.position)
            except:
                cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
                self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
                self.dialog.open()
     
    def show_custom_bottom_sheet(self):
        self.custom_sheet = MDCustomBottomSheet(screen=Factory.ContentCustomSheet())
        self.custom_sheet.open()
    
    def frigo(self):
        self.position = "frigo"
        self.callback(self.position)
        
    def congela(self):
        self.position = "Congelatore"
        self.callback(self.position)
        
    def credenza(self):
        self.position = "Credenza"
        self.callback(self.position)
        
    def create_wish_element(self):
        name = self.strng.get_screen('wishlistscreen').ids.name.text
        market = self.strng.get_screen('wishlistscreen').ids.market.text
        quantity = self.strng.get_screen('wishlistscreen').ids.quantity.text
        if name.split() == []:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Mancato inserimento',text = 'Compilare i campi necessari',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        else:
            try:
                data = {
                    name: {
                        'Supermercato': market,
                        'Quantità': quantity
                    }
                }
                database.child('users').child(self.username).child('Spesa').update(data, self.usr['idToken'])
                self.wish_list()
            except:
                cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
                self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
                self.dialog.open()
            
    def get_wish_element(self, instance):
        key_select_wish_item = ""
        for key in self.select_wish_item:
            if key == instance.text:
                key_select_wish_item = key
        if key_select_wish_item != "":
            self.select_wish_item.pop(key_select_wish_item)
        else:
            self.select_wish_item[instance.text] = instance.text
    
    def upgrade_wish_element(self):
        try:
            for key in self.select_wish_item:
                database.child('users').child(self.username).child('Spesa').child(key).remove(self.usr['idToken'])
            self.select_wish_item.clear()
            self.wish_list()
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'Si è verificato un errore nella ricerca degli elementi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
     
    def wish_list(self):
        try:
            self.strng.get_screen('wishlistscreen').ids.name.text = ""
            self.strng.get_screen('wishlistscreen').ids.market.text = ""
            self.strng.get_screen('wishlistscreen').ids.quantity.text = ""
            self.strng.get_screen('wishlistscreen').ids.scroll.clear_widgets()
            user_info = database.child('users').child(self.username).child('Spesa').get(self.usr['idToken'])
            for key in user_info.val():
                info = database.child('users').child('gabbro').child('Spesa').child(key).get(self.usr['idToken'])
                _text = {}
                c = 0
                _text[c] = key
                for key_ in info:
                    c = c + 1
                    _text[c] = key_.val()
                    if c == 2:
                        self.strng.get_screen('wishlistscreen').ids.scroll.add_widget(
                            ListItemWithCheckbox(text=_text[0], secondary_text=_text[2], tertiary_text=_text[1])
                        )
            self.strng.get_screen('wishlistscreen').manager.current = 'wishlistscreen'
        except:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Database',text = 'La lista della spesa è vuota',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
    def signup(self):
        signupEmail = self.strng.get_screen('signupscreen').ids.signup_email.text
        signupUsername = self.strng.get_screen('signupscreen').ids.signup_username.text
        signupPassword = self.strng.get_screen('signupscreen').ids.signup_password.text
        if signupEmail.split() == [] or signupPassword.split() == [] or signupUsername.split() == []:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Mancato inserimento',text = 'Compilare i campi necessari',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        if len(signupUsername.split())>1:
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Scrittura username errata',text = 'Inserire un nome senza spazi',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
        else:
            try:
                user = auth.sign_in_with_email_and_password(signupEmail, signupPassword)
                data = {"Password": signupPassword}
                database.child('users').child(signupUsername).update(data, user['idToken'])
                print("logged in")
            except:
                print("failed to log in")
                cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
                self.dialog = MDDialog(title = 'Autenticazione',text = 'Accesso negato, verificare le credenziali',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
                self.dialog.open()

    def login(self):
        try:
            loginEmail = self.strng.get_screen('loginscreen').ids.signup_email.text
            loginPassword = self.strng.get_screen('loginscreen').ids.login_password.text
            self.login_check = False
            user = auth.sign_in_with_email_and_password(loginEmail, loginPassword)
            self.usr = user
            user_info = database.child('users').get(user['idToken'])
            for key in user_info.val():
                users = database.child('users').child(key).get(user['idToken'])
                for key_ in users.val():
                    if key_ == 'Password':
                        usr = users.val()
                        pswrd = usr[key_]
                        if loginPassword == pswrd:
                            print("logged in")
                            self.username = key
                            self.login_check=True
                            self.strng.get_screen('mainscreen').manager.current = 'mainscreen'
                        else:
                            print("Password sconosciuta")
                            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
                            self.dialog = MDDialog(title = 'Autenticazione',text = 'Accesso negato, verificare le credenziali',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
                            self.dialog.open()
        except:
            print("failed to log in")
            cancel_btn_username_dialogue = MDFlatButton(text = 'Chiudi',on_release = self.close_username_dialog)
            self.dialog = MDDialog(title = 'Autenticazione',text = 'Accesso negato, errore di sistema',size_hint = (0.7,0.2),buttons = [cancel_btn_username_dialogue])
            self.dialog.open()
           
    def close_username_dialog(self,obj):
        self.dialog.dismiss()
        
    def verify_credential(self):
        if email != "" and password != "":
            self.strng.get_screen('loginscreen').ids.signup_email.text = email
            self.strng.get_screen('loginscreen').ids.login_password.text = password
            
    def username_changer(self):
        if self.login_check:
            self.strng.get_screen('mainscreen').ids.username_info.text = f"Benvenuto {self.username}\nScegli cosa fare!"
            
Inventario().run()