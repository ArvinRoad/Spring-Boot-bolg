package com.cxkj.bolg.web.admin;

import com.cxkj.bolg.pojo.Tag;
import com.cxkj.bolg.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 *  Created by Arvin on 2021/2/7.
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "/admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("type",new Tag());
        return "/admin/tags-input";
    }

    @GetMapping("tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("type",tagService.getTag(id));
        return "/admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tagname = tagService.getTagByName(tag.getName());
        if (tagname != null){
            result.rejectValue("name","nameError","管理员大大，这个标签已经有了。((٩(//̀Д/́/)۶))做人要专一哦！");
        }
        if (result.hasErrors()){
            return "/admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null){
            attributes.addFlashAttribute("message","新增失败 ﾍ(;´Д｀ﾍ),管理员大大重新试下吧");
        }else {
            attributes.addFlashAttribute("message","新增成功 ≖‿≖✧ 快去发布新内容吧");
        }
        return "redirect:../admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag,BindingResult result,@PathVariable Long id,RedirectAttributes attributes){
        Tag tagname = tagService.getTagByName(tag.getName());
        if (tagname != null){
            result.rejectValue("name","nameError","管理员大大，这个标签已经有了。((٩(//̀Д/́/)۶))做人要专一哦！");
        }
        if (result.hasErrors()){
            return "/admin/tags-input";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null){
            attributes.addFlashAttribute("message","更新失败（⊙o⊙）,管理员大大重新试下吧");
        }else {
            attributes.addFlashAttribute("message","更新成功 (≥◇≤) 快去发布新内容吧");
        }
        return "redirect:../tags";
    }

    @GetMapping("tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功,可能是管理员大大不喜欢它了吧(.◕ฺˇд ˇ◕ฺ)");
        return "redirect:../";
    }
}
