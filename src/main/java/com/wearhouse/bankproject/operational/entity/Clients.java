package com.wearhouse.bankproject.operational.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Accounts> accounts;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Loans> loans;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public List<Loans> getLoans() {
        return loans;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setLoans(List<Loans> loans) {
        this.loans = loans;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}