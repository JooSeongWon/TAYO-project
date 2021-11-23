package fun.tayo.app.dto;

import fun.tayo.app.common.util.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PagingBoardAndMember {
    private int workSpaceId;
    private int categoryId;
    private int memberId;
    private Paging paging;
}