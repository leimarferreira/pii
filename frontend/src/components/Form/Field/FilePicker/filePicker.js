import "./filePicker.css";

const FilePicker = ({ icon, label, onChange }) => {
  const toBase64 = (image, callback) => {
    let reader = new FileReader();
    reader.onloadend = () => {
      callback(reader.result);
    };
    reader.readAsDataURL(image);
  };

  const handleValueChange = (event) => {
    const file = event.target.files[0];
    toBase64(file, onChange);
  };

  return (
    <div className="form-field-filepicker-container">
      {label && <label className="form-field-filepicker-label">{label}</label>}
      <div className="form-field form-field-filepicker">
        {icon && <span className="form-field-left-icon">{icon}</span>}
        <input type="file" onChange={onChange ? handleValueChange : () => {}} />
      </div>
    </div>
  );
};

export default FilePicker;
