import { forwardRef } from "react";
import Spinner from "./Spinner/Spinner";

interface Props {
  show: boolean,
  handleConfirm: (arg: any) => void,
  loading: boolean,
  message: string
}

const GenericDeleteModal = forwardRef<HTMLLabelElement, Props>(({ show, handleConfirm, loading, message }: Props, ref) => {
  return (
    <>
      <input
        type="checkbox"
        checked={show}
        readOnly
        id="my-modal-4"
        className="modal-toggle"
      />
      <label htmlFor="my-modal-4" className="modal cursor-pointer">
        <label ref={ref} className="modal-box relative">
          <p className="text-center mb-3">
            {message}
          </p>
          <div className="flex justify-center">
            <button className={`btn ${loading && 'bg-slate-400'}`} onClick={handleConfirm}>
              {loading ? (
                <Spinner white={true} size="sm" />
              ) : (
                "Confirm"
              )}
            </button>
          </div>
        </label>
      </label>
    </>
  );
});
 
export default GenericDeleteModal;