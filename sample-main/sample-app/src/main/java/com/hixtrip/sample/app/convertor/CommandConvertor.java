package com.hixtrip.sample.app.convertor;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.vo.SampleVO;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.sample.model.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommandConvertor {

    CommandConvertor INSTANCE = Mappers.getMapper(CommandConvertor.class);

    CommandPay commandPayDtoToCommandPay(CommandPayDTO commandPayDTO);
}
