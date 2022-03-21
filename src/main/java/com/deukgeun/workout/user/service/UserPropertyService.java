package com.deukgeun.workout.user.service;

import com.deukgeun.workout.user.domain.UserProperty;
import com.deukgeun.workout.user.dto.UserPropertyDto;
import com.deukgeun.workout.user.repository.UserPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserPropertyService {

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    public UserPropertyService(UserPropertyRepository userPropertyRepository) {
        this.userPropertyRepository = userPropertyRepository;
    }

    public UserProperty saveOrUpdate(UserPropertyDto userPropertyDto) throws Exception {
        UserProperty userProperty = userPropertyRepository.findById(userPropertyDto.getId())
                .orElseThrow(() -> new Exception("Request id is not valid."));

        userProperty.setAge(userPropertyDto.getAge());
        userProperty.setHeight(userPropertyDto.getHeight());
        userProperty.setBodyFat(userPropertyDto.getHeight());
        userProperty.setWeight(userPropertyDto.getWeight());

        return userPropertyRepository.save(userProperty);
    }
}
