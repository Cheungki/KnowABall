package comsoftware.engine.entity;

import lombok.Data;

@Data
public class Triple {
    private String head, relation, tail;

    public Triple(String a, String b, String c) {
        head = a;
        relation = b;
        tail = c;
    }

    public String getHead() {
        return head;
    }

    public String getRelation() {
        return relation;
    }

    public String getTail() {
        return tail;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
}
