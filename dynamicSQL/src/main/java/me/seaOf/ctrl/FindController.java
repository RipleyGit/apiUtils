package me.seaOf.ctrl;

import me.seaOf.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//测试通用Mapper的工具
@Controller
public class FindController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/findCount")
	@ResponseBody
	public int findCount(){
		return itemService.findCount();
	}

}
