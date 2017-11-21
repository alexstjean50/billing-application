package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.BillStatus;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.entity.DiscountEntity;
import ca.ulaval.glo4002.billing.persistence.entity.ItemEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.ulaval.glo4002.commons.AssertHelpers.assertHasExactlyXNotNullElements;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BillAssemblerTest
{
    private static final Identity SOME_IDENTITY = new Identity(0);
    private static final Instant SOME_DATE = Instant.now();
    private static final DueTerm SOME_DUE_TERM = DueTerm.IMMEDIATE;
    private static final long SOME_ID = 0;
    private static final long SOME_OTHER_ID = SOME_ID + 1;
    private static final long SOME_BILL_NUMBER = 1337;
    private static final BillStatus SOME_BILL_STATUS = BillStatus.ACCEPTED;
    private static final List<ItemEntity> SOME_ITEM_ENTITIES = new ArrayList<>();
    private static final List<DiscountEntity> SOME_DISCOUNT_ENTITIES = new ArrayList<>();
    private static final List<AllocationEntity> SOME_ALLOCATION_ENTITIES = new ArrayList<>();
    private static final List<Item> SOME_ITEMS = new ArrayList<>();
    private static final List<Discount> SOME_DISCOUNTS = new ArrayList<>();
    private static final List<Allocation> SOME_ALLOCATIONS = new ArrayList<>();
    private static final long SOME_OTHER_BILL_NUMBER = 69;
    private static final BillStatus SOME_OTHER_BILL_STATUS = BillStatus.CANCELLED;
    private static final Instant SOME_OTHER_DATE = Instant.ofEpochMilli(1);
    private static final DueTerm SOME_OTHER_DUE_TERM = DueTerm.DAYS90;
    private static final int SOME_COUNT = 12;
    private static final DiscountEntity SOME_DISCOUNT_ENTITY = mock(DiscountEntity.class);
    private static final ItemEntity SOME_ITEM_ENTITY = mock(ItemEntity.class);
    private static final Discount SOME_DISCOUNT = mock(Discount.class);
    private static final Item SOME_ITEM = mock(Item.class);
    private static final AllocationEntity SOME_ALLOCATION_ENTITY = mock(AllocationEntity.class);
    private static final Allocation SOME_ALLOCATION = mock(Allocation.class);
    private BillAssembler billAssembler;

    @Before
    public void initializeBillAssembler()
    {
        ItemAssembler itemAssembler = mock(ItemAssembler.class);
        given(itemAssembler.toDomainModel(notNull())).willReturn(SOME_ITEM);
        given(itemAssembler.toPersistenceModel(notNull())).willReturn(SOME_ITEM_ENTITY);

        DiscountAssembler discountAssembler = mock(DiscountAssembler.class);
        given(discountAssembler.toDomainModel(notNull())).willReturn(SOME_DISCOUNT);
        given(discountAssembler.toPersistenceModel(notNull())).willReturn(SOME_DISCOUNT_ENTITY);

        AllocationAssembler allocationAssembler = mock(AllocationAssembler.class);
        given(allocationAssembler.toDomainModel(notNull())).willReturn(SOME_ALLOCATION);
        given(allocationAssembler.toPersistenceModel(notNull())).willReturn(SOME_ALLOCATION_ENTITY);

        this.billAssembler = new BillAssembler(itemAssembler, discountAssembler, allocationAssembler);
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenBillIdentityShouldCorrespondToBillEntityId()
    {
        long expectedBillEntityId = SOME_OTHER_ID;
        Identity someOtherIdentity = new Identity(expectedBillEntityId);
        Bill someBill = Bill.create(someOtherIdentity, SOME_BILL_NUMBER, SOME_DATE, SOME_DUE_TERM, SOME_ITEMS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedBillEntityId, result.getBillId());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenBillIdentityShouldCorrespondToBillEntityId()
    {
        Identity expectedBillIdentity = new Identity(SOME_OTHER_ID);
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setBillId(SOME_OTHER_ID);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedBillIdentity, result.getBillId());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenBillNumbersShouldBeEqual()
    {
        long expectedBillNumber = SOME_OTHER_BILL_NUMBER;
        Bill someBill = Bill.create(SOME_IDENTITY, expectedBillNumber, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, SOME_ITEMS, SOME_DISCOUNTS, SOME_ALLOCATIONS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedBillNumber, result.getBillNumber());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenBillNumbersShouldBeEqual()
    {
        long expectedBillNumber = SOME_OTHER_BILL_NUMBER;
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setBillNumber(expectedBillNumber);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedBillNumber, result.getBillNumber());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenBillStatusesShouldBeEqual()
    {
        BillStatus expectedBillStatus = SOME_OTHER_BILL_STATUS;
        Bill someBill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, expectedBillStatus, SOME_DATE,
                SOME_DUE_TERM, SOME_ITEMS, SOME_DISCOUNTS, SOME_ALLOCATIONS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedBillStatus, result.getStatus());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenBillStatusesShouldBeEqual()
    {
        BillStatus expectedBillStatus = SOME_OTHER_BILL_STATUS;
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setStatus(expectedBillStatus);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedBillStatus, result.getStatus());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenCreationDatesShouldBeEqual()
    {
        Instant expectedCreationDate = SOME_OTHER_DATE;
        Bill someBill = Bill.create(SOME_BILL_NUMBER, expectedCreationDate, SOME_DUE_TERM, SOME_ITEMS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedCreationDate, result.getCreationDate());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenCreationDatesShouldBeEqual()
    {
        Instant expectedCreationDate = SOME_OTHER_DATE;
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setCreationDate(expectedCreationDate);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedCreationDate, result.getCreationDate());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenEffectiveDatesShouldBeEqual()
    {
        Instant expectedEffectiveDate = SOME_OTHER_DATE;
        Bill someBill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, expectedEffectiveDate,
                SOME_DUE_TERM, SOME_ITEMS, SOME_DISCOUNTS, SOME_ALLOCATIONS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedEffectiveDate, result.getEffectiveDate());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenEffectiveDatesShouldBeEqual()
    {
        Instant expectedEffectiveInstant = SOME_OTHER_DATE;
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setEffectiveDate(expectedEffectiveInstant);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedEffectiveInstant, result.getEffectiveDate());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenDueTermsShouldBeEqual()
    {
        DueTerm expectedDueTerm = SOME_OTHER_DUE_TERM;
        Bill someBill = Bill.create(SOME_BILL_NUMBER, SOME_DATE, expectedDueTerm, SOME_ITEMS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertEquals(expectedDueTerm, result.getDueTerm());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenDueTermsShouldBeEqual()
    {
        DueTerm expectedDueTerm = SOME_OTHER_DUE_TERM;
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setDueTerm(expectedDueTerm);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertEquals(expectedDueTerm, result.getDueTerm());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenAllItemsShouldBeConverted()
    {
        int expectedItemEntityCount = SOME_COUNT;
        List<Item> someItems = Collections.nCopies(expectedItemEntityCount, SOME_ITEM);
        Bill someBill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, someItems, SOME_DISCOUNTS, SOME_ALLOCATIONS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertHasExactlyXNotNullElements(expectedItemEntityCount, result.getItemEntities());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenAllItemEntitiesShouldBeConverted()
    {
        int expectedItemCount = SOME_COUNT;
        List<ItemEntity> someItemEntities = Collections.nCopies(expectedItemCount, SOME_ITEM_ENTITY);
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setItemEntities(someItemEntities);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertHasExactlyXNotNullElements(expectedItemCount, result.getItems());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenAllDiscountsShouldBeConverted()
    {
        int expectedDiscountEntityCount = SOME_COUNT;
        List<Discount> someDiscounts = Collections.nCopies(expectedDiscountEntityCount, SOME_DISCOUNT);
        Bill someBill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, SOME_ITEMS, someDiscounts, SOME_ALLOCATIONS);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertHasExactlyXNotNullElements(expectedDiscountEntityCount, result.getDiscountEntities());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenAllDiscountEntitiesShouldBeConverted()
    {
        int expectedDiscountCount = SOME_COUNT;
        List<DiscountEntity> someDiscountEntities = Collections.nCopies(expectedDiscountCount, SOME_DISCOUNT_ENTITY);
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setDiscountEntities(someDiscountEntities);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertHasExactlyXNotNullElements(expectedDiscountCount, result.getDiscounts());
    }

    @Test
    public void givenABill_whenConvertedToBillEntity_thenAllAllocationsShouldBeConverted()
    {
        int expectedAllocationEntityCount = SOME_COUNT;
        List<Allocation> someAllocations = Collections.nCopies(expectedAllocationEntityCount, SOME_ALLOCATION);
        Bill someBill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, SOME_ITEMS, SOME_DISCOUNTS, someAllocations);

        BillEntity result = this.billAssembler.toPersistenceModel(someBill);

        assertHasExactlyXNotNullElements(expectedAllocationEntityCount, result.getAllocationEntities());
    }

    @Test
    public void givenABillEntity_whenConvertedToBill_thenAllAllocationEntitiesShouldBeConverted()
    {
        int expectedAllocationCount = SOME_COUNT;
        List<AllocationEntity> someAllocationEntities = Collections.nCopies(expectedAllocationCount,
                SOME_ALLOCATION_ENTITY);
        BillEntity someBillEntity = createSomeBillEntity();
        someBillEntity.setAllocationEntities(someAllocationEntities);

        Bill result = this.billAssembler.toDomainModel(someBillEntity);

        assertHasExactlyXNotNullElements(expectedAllocationCount, result.getAllocations());
    }

    private BillEntity createSomeBillEntity()
    {
        return new BillEntity(SOME_ID, SOME_BILL_NUMBER, SOME_BILL_STATUS, SOME_DATE, SOME_DATE,
                SOME_DUE_TERM, SOME_ITEM_ENTITIES, SOME_DISCOUNT_ENTITIES, SOME_ALLOCATION_ENTITIES);
    }
}
