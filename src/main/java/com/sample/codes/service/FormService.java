package com.sample.codes.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.codes.entity.FormEntity;
import com.sample.codes.model.FormModel;
import com.sample.codes.entity.Payment;
import com.sample.codes.repository.FormRepository;
import com.sample.codes.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FormService {

    public static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Value("${razorpay.keyId}")
    private String keyId;

    @Autowired
    FormRepository formRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    RazorpayClient razorpayClient;

    public FormModel submitForm(FormModel model) throws RazorpayException {
        FormEntity formEntity = new FormEntity();

        formEntity.setFullName(model.getFullName());
        formEntity.setEmail(model.getEmail());
        formEntity.setPhoneNumber(model.getPhoneNumber());
        formEntity.setGender(model.getGender());
        formEntity.setDob(model.getDob());
        formEntity.setBio(model.getShortBio());
        formEntity.setResume(model.getResume());

        FormEntity entity = formRepository.save(formEntity);

        int amountInPaise = 50 * 100;

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "rec_" + formEntity.getId());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);

        Payment resp = new Payment();
        resp.setOrderId(order.get("id"));
        resp.setAmount(amountInPaise);
        resp.setAmount(order.get("currency"));
        resp.setKeyId(keyId);
        resp.setFormEntityId(entity.getId());

        return model;
    }

    public List<FormModel> getForms() {
        List<FormModel> formModels = new ArrayList<>();

        List<FormEntity> formEntities = formRepository.findAll();
        List<Long> ids = formEntities.stream().map(FormEntity::getId).toList();

        List<Payment> paymentList = paymentRepository.findAllByFormEntityIds(ids);
        Map<Long, List<Payment>> paymentMap = paymentList.stream()
                .collect(Collectors.groupingBy(Payment::getFormEntityId));

        return formEntities.stream().map(formEntity -> {
            FormModel model = new FormModel();
            model.setFullName(formEntity.getFullName());
            model.setEmail(formEntity.getEmail());
            model.setPhoneNumber(formEntity.getPhoneNumber());
            model.setGender(formEntity.getGender());
            model.setDob(formEntity.getDob());
            model.setShortBio(formEntity.getBio());
            try {
                model.setResumeFile(getResumeFile(formEntity.getResume()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            model.setResume(formEntity.getResume());
            model.setPayments(paymentMap.get(formEntity.getId()));
            return model;
        }).toList();
    }

    public byte[] getResumeFile(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

}
