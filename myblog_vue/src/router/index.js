import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import 'element-ui/lib/theme-chalk/index.css'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import MyblogIndex from '@/myblog/blog_index.vue'
import MyblogReadArticle from '@/myblog/blog_read_article.vue'
import MyblogWriteArticle from '@/myblog/blog_write_article.vue'
import MyblogLogin from '@/myblog/blog_login.vue'
import MyblogCategory from '@/myblog/blog_category.vue'
import MyblogDownloadPage from '@/myblog/blog_download_page.vue'
import MyblogUserInfoPage from '@/myblog/blog_user_info_page.vue'
import MyblogMessage from '@/myblog/blog_message.vue'
import MyblogLife from '@/myblog/blog_life.vue'
import MyblogAdminIndex from '@/myblog_admin/myblog_admin_index.vue'
import MyblogAdminUserList from '@/myblog_admin/myblog_admin_user_list.vue'
import MyblogAdminArticleList from '@/myblog_admin/myblog_admin_article_list.vue'
import MyblogAdminUserFileList from '@/myblog_admin/myblog_admin_user_file_list.vue'
import MyblogAdminUserCommitList from '@/myblog_admin/myblog_admin_commit_list.vue'
import MyblogAdminUserRoleList from '@/myblog_admin/myblog_admin_user_role_list.vue'
import MyblogAdminRoleList from '@/myblog_admin/myblog_admin_role_list.vue'
import MyblogAdminLoginList from '@/myblog_admin/myblog_admin_login_list.vue'
import MyblogAdminCodeList from '@/myblog_admin/myblog_admin_code_list.vue'
import MyblogAdminVerifyList from '@/myblog_admin/myblog_admin_verify_list.vue'





import {
    Pagination,
    Dialog,
    Autocomplete,
    Dropdown,
    DropdownMenu,
    DropdownItem,
    Menu,
    Submenu,
    MenuItem,
    MenuItemGroup,
    Input,
    InputNumber,
    Radio,
    RadioGroup,
    RadioButton,
    Checkbox,
    CheckboxButton,
    CheckboxGroup,
    Switch,
    Select,
    Option,
    OptionGroup,
    Button,
    ButtonGroup,
    Table,
    TableColumn,
    DatePicker,
    TimeSelect,
    TimePicker,
    Popover,
    Tooltip,
    Breadcrumb,
    BreadcrumbItem,
    Form,
    FormItem,
    Tabs,
    TabPane,
    Tag,
    Tree,
    Alert,
    Slider,
    Icon,
    Row,
    Col,
    Upload,
    Progress,
    Spinner,
    Badge,
    Card,
    Rate,
    Steps,
    Step,
    Carousel,
    CarouselItem,
    Collapse,
    CollapseItem,
    Cascader,
    ColorPicker,
    Transfer,
    Container,
    Header,
    Aside,
    Main,
    Footer,
    Timeline,
    TimelineItem,
    Link,
    Divider,
    Image,
    Calendar,
    Backtop,
    PageHeader,
    CascaderPanel,
    Loading,
    MessageBox,
    Message,
    Notification,
    Avatar
} from 'element-ui'

import VueCookies from 'vue-cookies'
Vue.use(Avatar)
Vue.use(VueCookies)
Vue.use(Pagination)
Vue.use(Dialog)
Vue.use(Autocomplete)
Vue.use(Dropdown)
Vue.use(DropdownMenu)
Vue.use(DropdownItem)
Vue.use(Menu)
Vue.use(Submenu)
Vue.use(MenuItem)
Vue.use(MenuItemGroup)
Vue.use(Input)
Vue.use(InputNumber)
Vue.use(Radio)
Vue.use(RadioGroup)
Vue.use(RadioButton)
Vue.use(Checkbox)
Vue.use(CheckboxButton)
Vue.use(CheckboxGroup)
Vue.use(Switch)
Vue.use(Select)
Vue.use(Option)
Vue.use(OptionGroup)
Vue.use(Button)
Vue.use(ButtonGroup)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(DatePicker)
Vue.use(TimeSelect)
Vue.use(TimePicker)
Vue.use(Popover)
Vue.use(Tooltip)
Vue.use(Breadcrumb)
Vue.use(BreadcrumbItem)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Tabs)
Vue.use(TabPane)
Vue.use(Tag)
Vue.use(Tree)
Vue.use(Alert)
Vue.use(Slider)
Vue.use(Icon)
Vue.use(Row)
Vue.use(Col)
Vue.use(Upload)
Vue.use(Progress)
Vue.use(Spinner)
Vue.use(Badge)
Vue.use(Card)
Vue.use(Rate)
Vue.use(Steps)
Vue.use(Step)
Vue.use(Carousel)
Vue.use(CarouselItem)
Vue.use(Collapse)
Vue.use(CollapseItem)
Vue.use(Cascader)
Vue.use(ColorPicker)
Vue.use(Transfer)
Vue.use(Container)
Vue.use(Header)
Vue.use(Aside)
Vue.use(Main)
Vue.use(Footer)
Vue.use(Timeline)
Vue.use(TimelineItem)
Vue.use(Link)
Vue.use(Divider)
Vue.use(Image)
Vue.use(Calendar)
Vue.use(Backtop)
Vue.use(PageHeader)
Vue.use(CascaderPanel)

Vue.use(Loading.directive)
Vue.use(Router)

Vue.use(mavonEditor)


Vue.prototype.$loading = Loading.service
Vue.prototype.$msgbox = MessageBox
Vue.prototype.$alert = MessageBox.alert
Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$prompt = MessageBox.prompt
Vue.prototype.$notify = Notification
Vue.prototype.$message = Message


export default new Router({
        routes: [{
                path: '/hello',
                name: 'HelloWorld',
                component: HelloWorld
            }, {
                path: '/admin',
                name: "admin",
                component: MyblogAdminIndex,
                children: [{
                    path: '/admin/userlist',
                    name: 'userlist',
                    component: MyblogAdminUserList
                }, {
                    path: '/admin/userarticlelist',
                    name: 'userarticlelist',
                    component: MyblogAdminArticleList
                }, {
                    path: '/admin/usercommitlist',
                    name: 'usercommitlist',
                    component: MyblogAdminUserCommitList
                }, {
                    path: '/admin/userrole',
                    name: 'userrole',
                    component: MyblogAdminUserRoleList
                }, {
                    path: '/admin/rolelist',
                    name: 'rolelist',
                    component: MyblogAdminRoleList
                }, {
                    path: '/admin/userfilelist',
                    name: 'userfilelist',
                    component: MyblogAdminUserFileList
                }, {
                    path: '/admin/loginlist',
                    name: 'loginlist',
                    component: MyblogAdminLoginList
                }, {
                    path: '/admin/codelist',
                    name: 'codelist',
                    component: MyblogAdminCodeList
                },{
                    path: '/admin/verifylist',
                    name: 'verifylist',
                    component: MyblogAdminVerifyList
                },
                ]
            }, {
                path: '/Article/readArticle',
                name: 'readArticle',
                component: MyblogReadArticle
            }, {
                path: "/life",
                name: "life",
                component: MyblogLife
            }, {
                path: "/User",
                name: "User",
                component: MyblogUserInfoPage
            }, {
                path: "/message",
                name: "message",
                component: MyblogMessage
            }, {
                path: '/download',
                name: 'download',
                component: MyblogDownloadPage
            }, {
                path: '/category',
                name: 'category',
                component: MyblogCategory
            },
            {
                path: '/Article/writeArticle',
                name: 'writeArticle',
                component: MyblogWriteArticle
            }, {
                path: '/login',
                name: 'login',
                component: MyblogLogin
            },
            {
                path: '/',
                name: 'MyblogIndex',
                component: MyblogIndex
            }
        ]
    })
    //获取原型对象上的push函数
const originalPush = Router.prototype.push
    //修改原型对象中的push方法
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
