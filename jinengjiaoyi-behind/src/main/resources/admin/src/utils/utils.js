import storage from './storage';
import menu from './menu';
/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth(tableName,key,index=1) {
	let role = storage.get("role");
	if(!role){
		role = '管理员';
	}
	let menus = menu.list();
	for(let i=0;i<menus.length;i++){
		if(menus[i].roleName==role){
			for(let j=0;j<menus[i].backMenu.length;j++){
				for(let k=0;k<menus[i].backMenu[j].child.length;k++){
					if(tableName==menus[i].backMenu[j].child[k].tableName){
						let buttons = menus[i].backMenu[j].child[k].buttons
						for(let n in buttons) {
							if(buttons[n] == key) {
								return true
							}
						}
					}
				}
			}
		}
	}
	return false;
}

/**
 *  * 获取当前时间（yyyy-MM-dd hh:mm:ss）
 *   */
export function getCurDateTime() {
	let currentTime = new Date(),
	year = currentTime.getFullYear(),
	month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1,
	day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate(),
	hour = currentTime.getHours(),
	minute = currentTime.getMinutes(),
	second = currentTime.getSeconds();
	return year + "-" + month + "-" + day + " " +hour +":" +minute+":"+second;
}

/**
 *  * 获取当前日期（yyyy-MM-dd）
 *   */
export function getCurDate() {
	let currentTime = new Date(),
	year = currentTime.getFullYear(),
	month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1,
	day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate();
	return year + "-" + month + "-" + day;
}

export function getAge(identityCard) {
	var len = (identityCard + "").length;
	if (len == 0) {
		return 0;
	} else {
		if ((len != 15) && (len != 18))//身份证号码只能为15位或18位其它不合法
		{
			return 0;
		}
	}
	var strBirthday = "";
	if (len == 18)//处理18位的身份证号码从号码中得到生日和性别代码
	{
		strBirthday = identityCard.substr(6, 4) + "/" + identityCard.substr(10, 2) + "/" + identityCard.substr(12, 2);
	}
	if (len == 15) {
		strBirthday = "19" + identityCard.substr(6, 2) + "/" + identityCard.substr(8, 2) + "/" + identityCard.substr(10, 2);
	}
	//时间字符串里，必须是“/”
	var birthDate = new Date(strBirthday);
	var nowDateTime = new Date();
	var age = nowDateTime.getFullYear() - birthDate.getFullYear();
	if (nowDateTime.getMonth() < birthDate.getMonth() || (nowDateTime.getMonth() == birthDate.getMonth() && nowDateTime.getDate() < birthDate.getDate())) {
		age--;
	}
	return age;
}
