<tr>
    <td class="wing-col-item"><a href="#" class="del-quick-add">x</a></td>
    <td class="col-item"><?php echo $_POST['type']; ?></td>
    <td class="col-item"><a href="#<?php echo $_POST['item']; ?>"><?php echo $_POST['item']; ?></a></td>
    <td class="col-item">
        <input type="text" name="qty[]" class="qty-field" value="<?php echo $_POST['qty']; ?>"/>
    </td>
    <td class="col-item">
        <select name="uom">
            <option value="case">Case</option>
        </select>
    </td>
    <td class="col-item"><?php echo $_POST['job']; ?></td>
</tr>