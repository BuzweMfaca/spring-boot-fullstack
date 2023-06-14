import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";

const UpdateCustomerDrawer = ({ customer, fetchCustomers }) => {
    const {isOpen, onOpen, onClose} = useDisclosure();
    return(
        <div>
            <Button bg={'gray.400'}
                    color={'black'}
                    round={'full'}
                    _hover={{
                        transform: 'translateY(-2px)',
                        boxShadow: 'lg'
                    }}
                    onClick={onOpen}>
                Update customer
            </Button>

            <Drawer isOpen={isOpen} onClose={onClose} size={'x1'}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Update customer</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm
                            customer={customer}
                            fetchCustomers={fetchCustomers}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button colorScheme={'teal'}
                                onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>

        </div>
    )
}

export default UpdateCustomerDrawer;