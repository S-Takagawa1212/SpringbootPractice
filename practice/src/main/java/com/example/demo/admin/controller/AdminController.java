package com.example.demo.admin.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.common.service.ContactService;
import com.example.demo.contact.form.UpdateContactForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ContactService contactService;

    @GetMapping("/admin/contacts")
    public String showContacts(Model model) {

        model.addAttribute("contactDataForAdmin", contactService.getAllContact());

        return "admin/contactList";
    }

    @GetMapping("admin/contacts/{id}")
    public String showContactsDeteil(@PathVariable("id") Long id, Model model) {

        // idをkeyにしてDBから情報を取得して、それをmodelに格納する
        model.addAttribute("contactDataDetailForAdmin", contactService.getContactById(id));

        return "admin/contactDetail";
    }

    @GetMapping("admin/contacts/{id}/edit")
    public String showContactEdit(@PathVariable("id") Long id, Model model) {

        // idをkeyにしてDBから情報を取得して、それをmodelに格納する
        model.addAttribute("contactDataDetailForAdmin", contactService.getContactById(id));

        return "admin/contactEdit";
    }

    @PostMapping("admin/contacts/{id}/edit")
    public String editContact(@Validated @ModelAttribute("contactDataDetailForAdmin") UpdateContactForm form,
            BindingResult result,
            @PathVariable Long id) {

        if (result.hasErrors()) {
            return "admin/contactEdit";
        }

        // DBのデータを上書きする
        contactService.updateContact(form);

        // 更新日時をconsoleに出力
        System.out.println(LocalDateTime.now() + "に、ID:" + id + "のデータを上書きしました。");

        // TODO 上書きしました、という表示と変更後のデータが入力されたhtmlを表示させるべきかも
        return "redirect:/admin/contacts/{id}";
    }

    @PostMapping("admin/contacts/{id}/delete")
    public String deleteContact(@PathVariable Long id) {

        contactService.deleteContact(id);

        // 削除日時をconsoleに出力
        System.out.println(LocalDateTime.now() + "に、ID:" + id + "のデータを削除しました。");

        return "redirect:/admin/contacts";
    }

}
