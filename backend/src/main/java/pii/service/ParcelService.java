package pii.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.ParcelDTO;
import pii.exception.NotFoundException;
import pii.model.Expense;
import pii.model.Parcel;
import pii.repository.ExpenseRepository;
import pii.repository.ParcelRepository;

@Service
public class ParcelService {
	
	@Autowired
	private ParcelRepository repository;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	public List<ParcelDTO> findAllByInvoiceId(long invoiceId) {
		return repository.findAllByInvoiceId(invoiceId)
				.stream()
				.map(parcel -> {
					var expense = expenseRepository.findById(parcel.expenseId())
							.orElseThrow(() -> new NotFoundException(
									"Erro ao buscar despesa associada à parcela de id = " + parcel.id() + "."));
					
					return toDTO(parcel, expense);
				}).collect(Collectors.toList());
	}
	
	public List<ParcelDTO> findAllByExpenseId(long expenseId) {
		return repository.findAllByExpenseId(expenseId)
				.stream()
				.map(parcel -> {
					var expense = expenseRepository.findById(parcel.expenseId())
							.orElseThrow(() -> new NotFoundException(
									"Erro ao buscar despesa associada à parcela de id = " + parcel.id() + "."));
					
					return toDTO(parcel, expense);
				}).collect(Collectors.toList());
	}
	
	public Optional<Parcel> save(Parcel parcel) {
		return repository.save(parcel);
	}
	
	public boolean deleteAllByExpenseId(long expenseId) {
		return repository.deleteAllByExpenseId(expenseId);
	}
	
	private ParcelDTO toDTO(Parcel parcel, Expense expense) {
		return new ParcelDTO(
				parcel.id(),
				expense.description(),
				parcel.dateAsLong(),
				parcel.parcelNumber(),
				expense.numberOfParcels(),
				parcel.value(),
				parcel.expenseId(),
				parcel.invoiceId());
	}
}
